package es.uniovi.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.ServiceException;

public interface EscuelaService {

	Page<Escuela> getEscuelas(Integer page, Integer size);
	
	Escuela getEscuela(Long id) throws NoEncontradoException;

	Escuela addEscuela(Escuela escuela) throws ServiceException;

	Set<Sector> getSectores(Long id) throws NoEncontradoException;
	
	Sector getSector(Long idEscuela, Long idSector) throws NoEncontradoException;

	Sector addSector(Long idSector, Sector sector) throws ServiceException;

	Set<Via> getVias(Long idEscuela, Long idSector) throws ServiceException;

	Via getVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException;

	Via addVia(Long idEscuela, Long idSector, Via via) throws ServiceException;

}
