package es.uniovi.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import es.uniovi.domain.CierreTemporada;
import es.uniovi.domain.Croquis;
import es.uniovi.domain.Escuela;
import es.uniovi.domain.HorasDeSol;
import es.uniovi.domain.Sector;
import es.uniovi.domain.TrazoVia;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.PatchInvalidoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.CierreTemporadaRepository;
import es.uniovi.repository.CroquisRepository;
import es.uniovi.repository.EscuelaRepository;
import es.uniovi.repository.HorasDeSolRepository;
import es.uniovi.repository.SectorRepository;
import es.uniovi.repository.TrazoViaRepository;
import es.uniovi.repository.ViaRepository;
import es.uniovi.service.EscuelaService;
import es.uniovi.service.ImagenService;

@Service
public class EscuelaServiceImpl implements EscuelaService {

	private static final Supplier<? extends NoSuchElementException> INCONSISTENCIA_EXCEPTION_SUPPLIER = () ->
		new NoSuchElementException("Inconsistencia en los datos, recurso debería de existir");		

	@Autowired
	private EscuelaRepository escuelaRepository;

	@Autowired
	private SectorRepository sectorRepository;
	
	@Autowired
	private ViaRepository viaRepository;
	
	@Autowired
	private CroquisRepository croquisRepository;

	@Autowired
	private CierreTemporadaRepository cierreTemporadaReporitory;
	
	@Autowired
	private TrazoViaRepository trazoViaRepository;

	@Autowired
	private HorasDeSolRepository horasDeSolRepository;

	@Autowired
	private ImagenService imagenService;
	
	@Override
	public Page<Escuela> getEscuelas(Integer page, Integer size) {
		return escuelaRepository.findAll(PageRequest.of(page, size, Sort.by("nombre")));
	}

	@Override
	public Escuela getEscuela(Long id) throws NoEncontradoException {
		return doGetEscuela(id);
	}

	@Override
	@Transactional
	public Escuela addEscuela(Escuela escuela) throws RestriccionDatosException {
		try {
			escuela.setId(null);
			for (Sector sector : escuela.getSectores()) {
				asociaNuevoSector(escuela, sector);
			}
			for (CierreTemporada cierreTemporada : escuela.getCierresTemporada()) {
				asociaNuevoCierre(escuela, cierreTemporada);
			}
			return doSaveEscuela(escuela);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	/**
	 * Persiste la escuela pasada y todos sus sectores
	 * 
	 * @param escuela La nueva escuela
	 * @return La escuela persistida
	 */
	private Escuela doSaveEscuela(Escuela escuela) {
		Escuela saved = escuelaRepository.save(escuela);
		//escuela.getSectores().forEach(this::doSaveSector);
		return saved;
	}

	/**
	 * <li>Asocia la escuela pasada al sector pasado
	 * <li>Setea a null la id del sector
	 * <li>Asocia las vías del nuevo sector
	 * 
	 * @param escuela La escuela
	 * @param sector El sector
	 */
	private void asociaNuevoSector(Escuela escuela, Sector sector) {
		sector.setEscuela(escuela);
		sector.setId(null);
		for (Via via : sector.getVias()) {
			asociaSectorVia(sector, via);
		}
	}

	/**
	 * <li>Asocia el sector pasado a la vía pasada
	 * <li>Setea la id a null
	 * 
	 * @param sector
	 * @param via
	 */
	private void asociaSectorVia(Sector sector, Via via) {
		via.setSector(sector);
		via.setId(null);
	}

	@Override
	public Set<Sector> getSectores(Long id) throws NoEncontradoException {
		return sectorRepository.findByEscuela(doGetEscuela(id));
	}

	@Override
	public Sector getSector(Long idEscuela, Long idSector) throws NoEncontradoException {
		Escuela escuela = doGetEscuela(idEscuela);
		return doGetSectorDeEscuela(idSector, escuela);
	}

	@Override
	@Transactional
	public Sector addSector(Long idEscuela, Sector sector) throws ServiceException {
		try {
			asociaNuevoSector(doGetEscuela(idEscuela), sector);
			if (sectorRepository.existsByEscuelaAndNombre(sector.getEscuela(), sector.getNombre())) {
				throw new RestriccionDatosException(
						String.format("Nombre de sector ya existe en la escuela %s", sector.getEscuela().getId()));
			}
			doSaveSector(sector);
			return sector;
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	/**
	 * Persiste el sector pasado y todas sus vías
	 * 
	 * @param sector El nuevo sector a persistir
	 * @return El sector persistido
	 */
	private void doSaveSector(Sector sector) {
		sectorRepository.save(sector);
		sector.getVias().forEach(v -> viaRepository.save(v));
		if (sector.getHorasDeSol() != null) {
			sector.getHorasDeSol().setSector(sector);
			horasDeSolRepository.save(sector.getHorasDeSol());
		}
	}
	
	private Set<Via> clonaVias(Sector sector) {
		return sector
				.getVias()
				.stream()
				.map(via -> {
					Via clon = new Via();
					BeanUtils.copyProperties(via, clon);
					clon.setSector(sector);
					return clon;
				})
				.collect(Collectors.toSet());
	}

	@Override
	public Set<Via> getVias(Long idEscuela, Long idSector) throws NoEncontradoException {
		Escuela escuela = doGetEscuela(idEscuela);
		Sector sector = doGetSectorDeEscuela(idSector, escuela);
		return sector.getVias();
	}

	@Override
	public Via getVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException {
		Escuela escuela = doGetEscuela(idEscuela);
		Sector sector = doGetSectorDeEscuela(idSector, escuela);
		return  doGetViaDeSector(idVia, sector);
	}

	@Override
	public Via addVia(Long idEscuela, Long idSector, Via via) throws ServiceException {
		try {
			Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
			asociaSectorVia(sector, via);
			return viaRepository.save(via);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	private Escuela doGetEscuela(Long id) throws NoEncontradoException {
		return escuelaRepository.findById(id).orElseThrow(() -> new NoEncontradoException("escuela", id));
	}

	private Sector doGetSectorDeEscuela(Long idSector, Escuela escuela) throws NoEncontradoException {
		if (escuela.getSectores() == null
				|| escuela
					.getSectores()
					.stream()
					.map(Sector::getId)
					.filter(Objects::nonNull)
					.filter(id -> id.equals(idSector))
					.count() == 0) {
			throw new NoEncontradoException("escuela/sector", escuela.getId() + "/" + idSector);
		}
		return sectorRepository.findById(idSector).orElseThrow(INCONSISTENCIA_EXCEPTION_SUPPLIER);
	}

	private Via doGetViaDeSector(Long idVia, Sector sector) throws NoEncontradoException {
		if (sector.getVias() == null
				|| sector
					.getVias()
					.stream()
					.map(Via::getId)
					.filter(Objects::nonNull)
					.filter(id -> id.equals(idVia))
					.count() == 0) {
			throw new NoEncontradoException("sector/via", sector.getId() + "/" + idVia);
		}
		return viaRepository.findById(idVia).orElseThrow(INCONSISTENCIA_EXCEPTION_SUPPLIER);
	}

	@Override
	public Escuela actualizaEscuela(Long id, JsonPatch jsonPatch) throws ServiceException {
		try {
			Escuela escuela = doGetEscuela(id);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonPatched = jsonPatch.apply(objectMapper.convertValue(escuela, JsonNode.class));
			Escuela escuelaPatched = objectMapper.treeToValue(jsonPatched, Escuela.class);
			checkValido(escuelaPatched);
			checkNombreExiste(escuela, escuelaPatched);
			checkPatchValido(escuela, escuelaPatched);
			return doActualizaEscuela(escuelaPatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			throw new PatchInvalidoException(e.getLocalizedMessage());
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		} 
	}

	private Escuela doActualizaEscuela(Escuela escuela) {
		Set<Via> vias = new HashSet<>();
		for (Sector sector : escuela.getSectores()) {
			sector.setEscuela(escuela);
			// sectorRepository.save(sector);
			for (Via via : sector.getVias()) {
				via.setSector(sector);
				// viaRepository.save(via);
			}
			vias.addAll(clonaVias(sector));
		}
		Escuela saved = escuelaRepository.save(escuela);
//		entityManager.merge(saved);
//		for (Via via : vias) {
//			viaRepository.save(via);
//		}
		return saved;
	}

	/**
	 * Valida una escuela
	 * 
	 * @param escuela La escuela a validar
	 */
	private void checkValido(Escuela escuela) {
		Set<ConstraintViolation<Escuela>> violations = Validation
				.buildDefaultValidatorFactory()
				.getValidator()
				.validate(escuela);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		if (escuela.getSectores() == null) {
			escuela.setSectores(new HashSet<>());
		}
		escuela.getSectores().forEach(this::checkValido);
	}

	/**
	 * Valida un sector
	 * 
	 * @param sector El sector a validar
	 */
	private void checkValido(Sector sector) {
		Set<ConstraintViolation<Sector>> violations = Validation
				.buildDefaultValidatorFactory()
				.getValidator()
				.validate(sector);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
		sector.getVias().forEach(this::checkValido);
	}
	
	/**
	 * Valida una vía
	 * 
	 * @param via La vía a validar
	 */
	private void checkValido(Via via) {
		Set<ConstraintViolation<Via>> violations = Validation
				.buildDefaultValidatorFactory()
				.getValidator()
				.validate(via);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}		
	}	

	/**
	 * Este método impide que se modifique la id
	 * @param escuela
	 * @param escuelaPatched
	 * @throws PatchInvalidoException
	 */
	private void checkPatchValido(Escuela escuela, Escuela escuelaPatched) throws PatchInvalidoException {
		if (!escuelaPatched.equals(escuela)) {
			throw new PatchInvalidoException("Operación de patch no soportado");
		}
	}

	/**
	 * Valida que si se está cambiando el nombre de la escuela, este no exista ya
	 * 
	 * @param escuela        La escuela actual
	 * @param escuelaPatched La escuela modificada
	 * @throws RestriccionDatosException
	 */
	private void checkNombreExiste(Escuela escuela, Escuela escuelaPatched) throws RestriccionDatosException {
		if (!escuela.getNombre().equals(escuelaPatched.getNombre())
				&& escuelaRepository.existsByNombre(escuelaPatched.getNombre())) {
			throw new RestriccionDatosException("nombre: " + escuelaPatched.getNombre() + " ya existe");
		}
	}

	@Override
	public void deleteEscuela(Long id) throws NoEncontradoException {
		escuelaRepository.delete(doGetEscuela(id));		
	}

	@Override
	public void deleteSector(Long idEscuela, Long idSector) throws NoEncontradoException {
		Escuela escuela = doGetEscuela(idEscuela);
		Sector sector = escuela
				.getSectores()
				.stream()
				.filter(s -> idSector.equals(s.getId()))
				.findAny()
				.orElseThrow(() -> new NoEncontradoException("sector", idSector));
		escuela.getSectores().remove(sector);
		escuelaRepository.save(escuela);
		sectorRepository.delete(sector);
	}

	@Override
	public void deleteVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		Via via = sector
				.getVias()
				.stream()
				.filter(v -> idVia.equals(v.getId()))
				.findAny()
				.orElseThrow(() -> new NoEncontradoException("sector", idVia));
		sector.getVias().remove(via);
		sectorRepository.save(sector);
		viaRepository.delete(via);		
	}

	@Override
	public Escuela actualizaEscuela(Long id, Escuela escuela2) throws NoEncontradoException {
		Escuela escuela = doGetEscuela(id);
		escuela.setNombre(escuela2.getNombre());
		return escuelaRepository.save(escuela);
	}

	@Override
	public Sector actualizaSector(Long idEscuela, Long idSector, Sector sector2) throws NoEncontradoException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		sector.setLatitud(sector2.getLatitud());
		sector.setLongitud(sector2.getLongitud());
		sector.setNombre(sector2.getNombre());
		actualizaHorasDeSol(sector2, sector);
		return sectorRepository.save(sector);
	}

	private void actualizaHorasDeSol(Sector sector2, Sector sector) {
		if (sector.getHorasDeSol() != null) {
			if (sector2.getHorasDeSol() == null) {
				horasDeSolRepository.delete(sector.getHorasDeSol());
			} else {
				sector.getHorasDeSol().setInicio(sector2.getHorasDeSol().getInicio());
				sector.getHorasDeSol().setFin(sector2.getHorasDeSol().getFin());
				horasDeSolRepository.save(sector.getHorasDeSol());
			}
		} else if (sector2.getHorasDeSol() != null) {
			HorasDeSol horasDeSol = sector2.getHorasDeSol();
			horasDeSol.setSector(sector);
			if (horasDeSol != null) {
				horasDeSolRepository.save(horasDeSol);
			}
		}
	}

	@Override
	public Via actualizaVia(Long idEscuela, Long idSector, Long idVia, Via via2) throws NoEncontradoException {
		Via via = doGetViaDeSector(idVia, doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela)));
		via.setGrado(via2.getGrado());
		via.setLongitud(via2.getLongitud());
		via.setNombre(via2.getNombre());
		via.setNumeroChapas(via2.getNumeroChapas());
		return viaRepository.save(via);
	}

	@Override
	public List<Croquis> getCroquis(Long idEscuela, Long idSector) throws NoEncontradoException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		return croquisRepository.findBySector(sector);
	}

	@Override
	public Croquis getCroquis(Long idEscuela, Long idSector, Long idCroquis) throws NoEncontradoException {
		return doGetCroquisSector(idCroquis, doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela)));
	}

	@Override
	public Croquis addCroquis(Long idEscuela, Long idSector, Croquis croquis) throws ServiceException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		croquis.setSector(sector);
		imagenService.checkImagen(croquis.getImagen());
		try {
			return croquisRepository.save(croquis);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public void deleteCroquis(Long idEscuela, Long idSector, Long idCroquis) throws NoEncontradoException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		Croquis croquis = sector
			.getCroquis()
			.stream()
			.filter(c -> c.getId().equals(idCroquis))
			.findAny()
			.orElseThrow(() -> new NoEncontradoException(
					"escuela/sector/croquis",
					idEscuela + "/" + idSector + "/" + idCroquis));
		croquisRepository.delete(croquis);		
	}

	@Override
	public TrazoVia addTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia,
			TrazoVia trazoVia) throws ServiceException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		Via via = doGetViaDeSector(idVia, sector);
		Croquis croquis = doGetCroquisSector(idCroquis, sector);
		trazoVia.setCroquis(croquis);
		trazoVia.setVia(via);
		if (trazoViaRepository.existsByCroquisAndVia(croquis, via)) {
			throw new RestriccionDatosException("ya existe el trazo para croquis/via: " + idCroquis + "/" + idVia);
		}
		try {
			return trazoViaRepository.save(trazoVia);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	private Croquis doGetCroquisSector(Long idCroquis, Sector sector) throws NoEncontradoException {
		if (sector.getCroquis() == null ||
				sector
				.getCroquis()
				.stream()
				.map(Croquis::getId)
				.filter(Objects::nonNull)
				.filter(id -> id.equals(idCroquis))
				.count() == 0) {
			throw new NoEncontradoException("sector/croquis", sector.getId() + "/" + idCroquis);
		}
		return croquisRepository.findById(idCroquis).orElseThrow(INCONSISTENCIA_EXCEPTION_SUPPLIER);
	}

	@Override
	public TrazoVia updateTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia,
			TrazoVia trazoViaActualizado) throws NoEncontradoException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		Croquis croquis = doGetCroquisSector(idCroquis, sector);
		Via via = doGetViaDeSector(idVia, sector);
		TrazoVia trazoVia = trazoViaRepository
				.findByCroquisAndVia(croquis, via)
				.orElseThrow(() -> new NoEncontradoException("croquis, via", idCroquis + ", " + idVia));
		trazoVia.setPuntos(trazoViaActualizado.getPuntos());
		return trazoViaRepository.save(trazoVia);
	}

	@Override
	public void deleteTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia) throws NoEncontradoException {
		Sector sector = doGetSectorDeEscuela(idSector, doGetEscuela(idEscuela));
		Croquis croquis = doGetCroquisSector(idCroquis, sector);
		Via via = doGetViaDeSector(idVia, sector);
		TrazoVia trazoVia = trazoViaRepository
				.findByCroquisAndVia(croquis, via)
				.orElseThrow(() -> new NoEncontradoException("croquis, via", idCroquis + ", " + idVia));
		trazoViaRepository.delete(trazoVia);	
	}

	@Override
	public CierreTemporada addCierreTemporada(Long idEscuela, CierreTemporada cierreTemporada) throws ServiceException {
		asociaNuevoCierre(doGetEscuela(idEscuela), cierreTemporada);
		return cierreTemporadaReporitory.save(cierreTemporada);
	}
	
	/**
	 * <li>Asocia la escuela pasada al cierre pasado
	 * <li>Setea a null la id del cierre
	 * 
	 * @param escuela         La escuela
	 * @param cierreTemporada El cierre
	 */
	private void asociaNuevoCierre(Escuela escuela, CierreTemporada cierreTemporada) {
		cierreTemporada.setEscuela(escuela);
		cierreTemporada.setId(null);
	}

	@Override
	public void deleteCierre(Long idEscuela, Long idCierre) throws ServiceException {
		Escuela escuela = doGetEscuela(idEscuela);
		CierreTemporada cierreTemporada = doGetCierreDeEscuela(idCierre, escuela);
		escuela.getCierresTemporada().remove(cierreTemporada);
		escuelaRepository.save(escuela);
		cierreTemporadaReporitory.delete(cierreTemporada);		
	}

	private CierreTemporada doGetCierreDeEscuela(Long idCierre, Escuela escuela) throws NoEncontradoException {
		if (escuela.getCierresTemporada() == null
				|| escuela
					.getCierresTemporada()
					.stream()
					.map(CierreTemporada::getId)
					.filter(Objects::nonNull)
					.filter(id -> id.equals(idCierre))
					.count() == 0) {
			throw new NoEncontradoException("escuela/cierre", escuela.getId() + "/" + idCierre);
		}
		return cierreTemporadaReporitory.findById(idCierre).orElseThrow(INCONSISTENCIA_EXCEPTION_SUPPLIER);
	}
	
}
