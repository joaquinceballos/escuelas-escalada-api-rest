package es.uniovi.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.github.fge.jsonpatch.JsonPatch;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.ServiceException;

public interface EscuelaService {

	/**
	 * Obtine la página de escuelas para los parámetros pasados
	 * 
	 * @param page El número de página
	 * @param size El tamaño de la página
	 * @return La página de resultados
	 */
	Page<Escuela> getEscuelas(Integer page, Integer size);

	/**
	 * Obtiene la Escuela cuyo id es pasado
	 * 
	 * @param id La id
	 * @return La Escuela buscada
	 * @throws NoEncontradoException Si no se han encontrado resultados
	 */
	Escuela getEscuela(Long id) throws NoEncontradoException;

	/**
	 * Persiste una nueva escuela y todos los sectores que contenga
	 * 
	 * @param escuela La Escuela
	 * @return La escuela persistida
	 * @throws ServiceException
	 */
	Escuela addEscuela(Escuela escuela) throws ServiceException;

	/**
	 * Obtiene todos los sectores de la escuela cuya id es pasada
	 * 
	 * @param id La id de la escuela
	 * @return La lista de sectores, si no hay ninguno una lista vacía
	 * @throws NoEncontradoException Si no existe escuela con la id pasada
	 */
	Set<Sector> getSectores(Long id) throws NoEncontradoException;

	/**
	 * Obtiene el sector cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector a buscar
	 * @return El sector
	 * @throws NoEncontradoException Si la id de escuela no existe o si el id de
	 *                               sector no pertenece a la escuela pasada
	 */
	Sector getSector(Long idEscuela, Long idSector) throws NoEncontradoException;

	/**
	 * Persiste un nuevo sector asociado a la escuela cuya id es pasada.
	 * 
	 * @param idEscuela La id de la escuela
	 * @param sector    El sector a persistir
	 * @return El sector persistido
	 * @throws ServiceException
	 */
	Sector addSector(Long idEscuela, Sector sector) throws ServiceException;

	/**
	 * Obtiene las vías del sector pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @return La lista de las vías del sector
	 * @throws ServiceException
	 */
	Set<Via> getVias(Long idEscuela, Long idSector) throws ServiceException;

	/**
	 * Obtiene la vía cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @return La vía
	 * @throws NoEncontradoException
	 */
	Via getVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException;

	/**
	 * Persiste la escuela pasada
	 * 
	 * @param idEscuela La id de las escuela
	 * @param idSector  La id del sector
	 * @param idVia     La vía a persistir
	 * @return La vía persistida
	 * @throws NoEncontradoException
	 */
	Via addVia(Long idEscuela, Long idSector, Via via) throws ServiceException;

	/**
	 * Actualiza la escuela cuyo id es pasado
	 * 
	 * @param id        La id de la escuela a modificar
	 * @param jsonPatch Las cambios a aplicar en la escuela
	 * @return La escuela modificada
	 * @throws ServiceException
	 */
	Escuela actualizaEscuela(Long id, JsonPatch jsonPatch) throws ServiceException;

}
