package es.uniovi.service.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.EscuelaRepository;
import es.uniovi.repository.SectorRepository;
import es.uniovi.repository.ViaRepository;
import es.uniovi.service.EscuelaService;

@Service
public class EscuelaServiceImpl implements EscuelaService {

	private static final Supplier<? extends NoSuchElementException> INCONSISTENCIA_EXCEPTION_SUPPLIER = () -> {
		return new NoSuchElementException("Inconsistencia en los datos, recurso debería de existir");
	};			

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

}
