package es.uniovi.service;

import es.uniovi.domain.Escuela;
import es.uniovi.exception.ServiceException;

public interface EscuelaService {

	Escuela getEscuela(Long id);

	Escuela addEscuela(Escuela escuela) throws ServiceException;	

}
