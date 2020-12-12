package es.uniovi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Escuela;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.EscuelaRepository;
import es.uniovi.service.EscuelaService;

@Service
public class EscuelaServiceImpl implements EscuelaService {

	@Autowired
	private EscuelaRepository escuelaRepository;

	@Override
	public Escuela getEscuela(Long id) {
		return escuelaRepository.findById(id).orElse(null);
	}

	@Override
	public Escuela addEscuela(Escuela escuela) throws ServiceException {
		try {
			return escuelaRepository.save(escuela);
		} catch(DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

}
