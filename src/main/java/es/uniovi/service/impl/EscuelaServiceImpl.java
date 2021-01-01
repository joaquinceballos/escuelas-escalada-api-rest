package es.uniovi.service.impl;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

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

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.PatchInvalidoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.EscuelaRepository;
import es.uniovi.repository.SectorRepository;
import es.uniovi.repository.ViaRepository;
import es.uniovi.service.EscuelaService;

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

	@Override
	public Page<Escuela> getEscuelas(Integer page, Integer size) {
		return escuelaRepository.findAll(PageRequest.of(page, size, Sort.by("nombre")));
	}

	@Override
	public Escuela getEscuela(Long id) throws NoEncontradoException {
		return doGetEscuela(id);
	}

	@Override
	public Escuela addEscuela(Escuela escuela) throws ServiceException {
		try {
			escuelaRepository.save(escuela);
			if (escuela.getSectores() != null) {
				for (Sector sector : escuela.getSectores()) {
					doAddSector(escuela, sector);
				}
			}
			return escuela;
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
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
	public Sector addSector(Long idEscuela, Sector sector) throws ServiceException {
		Escuela escuela = doGetEscuela(idEscuela);
		return doAddSector(escuela, sector); 
	}

	private Sector doAddSector(Escuela escuela, Sector sector) throws RestriccionDatosException {
		sector.setEscuela(escuela);
		try {
			sectorRepository.save(sector);
			if (sector.getVias() != null) {
				for (Via via : sector.getVias()) {
					doAddVia(sector, via);
				}
			}
			return sector;
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public Set<Via> getVias(Long idEscuela, Long idSector) throws ServiceException {
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
		Escuela escuela = doGetEscuela(idEscuela);
		Sector sector = doGetSectorDeEscuela(idSector, escuela);
		return doAddVia(sector, via); 		
	}

	private Via doAddVia(Sector sector, Via via) throws RestriccionDatosException {
		via.setSector(sector);
		try {
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

	@Transactional
	private Escuela doActualizaEscuela(Escuela escuela) {
		for (Sector sector : escuela.getSectores()) {
			sector.setEscuela(escuela);
			sectorRepository.saveAndFlush(sector);
			for (Via via : sector.getVias()) {
				via.setSector(sector);
				viaRepository.saveAndFlush(via);
			}
		}
		return escuelaRepository.save(escuela);
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
	
}
