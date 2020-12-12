package es.uniovi.service;

import java.util.List;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.ServiceException;

public interface EscuelaService {

	Escuela getEscuela(Long id) throws NoEncontradoException;

	Escuela addEscuela(Escuela escuela) throws ServiceException;

	List<Sector> getSectores(Long id) throws NoEncontradoException;

	Sector addSector(Long idSector, Sector sector) throws NoEncontradoException;

}
