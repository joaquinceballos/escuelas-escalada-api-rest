package es.uniovi.service;

import java.util.List;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.ServiceException;

public interface EscuelaService {

	Escuela getEscuela(Long id) throws NoEncontradoException;

	Escuela addEscuela(Escuela escuela) throws ServiceException;

	List<Sector> getSectores(Long id) throws NoEncontradoException;

	Sector addSector(Long idSector, Sector sector) throws ServiceException;

	List<Via> getVias(Long idEscuela, Long idSector) throws ServiceException;

	Via getVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException;

	Via addVia(Long idEscuela, Long idSector, Via via) throws ServiceException;

	Page<Escuela> getEscuelas(Integer page, Integer size);

}
