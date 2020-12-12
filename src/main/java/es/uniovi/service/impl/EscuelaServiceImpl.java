package es.uniovi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.EscuelaRepository;
import es.uniovi.repository.SectorRepository;
import es.uniovi.service.EscuelaService;

@Service
public class EscuelaServiceImpl implements EscuelaService {

	@Autowired
	private EscuelaRepository escuelaRepository;

	@Autowired
	private SectorRepository sectorRepository;

	@Override
	public Escuela getEscuela(Long id) throws NoEncontradoException {
		return doGetEscuela(id);
	}

	@Override
	public Escuela addEscuela(Escuela escuela) throws ServiceException {
		try {
			return escuelaRepository.save(escuela);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public List<Sector> getSectores(Long id) throws NoEncontradoException {
		return sectorRepository.findByEscuela(doGetEscuela(id));
	}

	@Override
	public Sector addSector(Long idSector, Sector sector) throws NoEncontradoException {
		Escuela escuela = doGetEscuela(idSector);
		sector.setEscuela(escuela);
		return sectorRepository.save(sector);
	}

	private Escuela doGetEscuela(Long id) throws NoEncontradoException {
		return escuelaRepository.findById(id).orElseThrow(() -> new NoEncontradoException("escuela", id));
	}

}
