package es.uniovi.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.github.fge.jsonpatch.JsonPatch;

import es.uniovi.domain.CierreTemporada;
import es.uniovi.domain.Croquis;
import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.TrazoVia;
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
	Set<Via> getVias(Long idEscuela, Long idSector) throws NoEncontradoException;

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

	/**
	 * Borra la escuela cuya id es pasada
	 * 
	 * @param id La id de la escuela a Borrar
	 */
	void deleteEscuela(Long id) throws NoEncontradoException;

	/**
	 * Borra el sector cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @throws NoEncontradoException
	 */
	void deleteSector(Long idEscuela, Long idSector) throws NoEncontradoException;

	/**
	 * Borra la vía cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @throws NoEncontradoException
	 */
	void deleteVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException;

	/**
	 * Actualiza todos los campos simples de la escuela cuyo id es pasado
	 * 
	 * @param id      La id de la escuela
	 * @param escuela La escuela
	 * @return La escuela actualizada
	 * @throws NoEncontradoException
	 */
	Escuela actualizaEscuela(Long id, Escuela escuela) throws NoEncontradoException;

	/**
	 * Actualiza todos los campos simples del sector cuyo id es pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param sector    El sector con los datos a actualizar
	 * @return El sector actualizado
	 * @throws NoEncontradoException
	 */
	Sector actualizaSector(Long idEscuela, Long idSector, Sector sector) throws NoEncontradoException;

	/**
	 * Actualiza todos los campos simples de la escuela pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @param via       La vía con los campos a actualizar
	 * @return La vía actualizada
	 * @throws NoEncontradoException
	 */
	Via actualizaVia(Long idEscuela, Long idSector, Long idVia, Via via) throws NoEncontradoException;

	/**
	 * Obtiene los croquis del sector pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @return La lista de croquis del sector
	 * @throws NoEncontradoException
	 */
	List<Croquis> getCroquis(Long idEscuela, Long idSector) throws NoEncontradoException;
	
	/**
	 * Obtiene el croquis pasado
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idSector  El id del sector
	 * @param idCroquis El id del croquis
	 * @return El croquis
	 * @throws NoEncontradoException
	 */
	Croquis getCroquis(Long idEscuela, Long idSector, Long idCroquis) throws NoEncontradoException;

	/**
	 * Añade nuevo Croquis al sector
	 * 
	 * @param idEscuela El id de las escuela
	 * @param idSector  El id del sector
	 * @param croquis   El croquis
	 * @return El croquis persistido
	 * @throws ServiceException 
	 */
	Croquis addCroquis(Long idEscuela, Long idSector, Croquis croquis) throws ServiceException;

	/**
	 * Borra el croquis cuya id es pasada
	 * 
	 * @param idEscuela El id de las escuela
	 * @param idSector  El id del sector
	 * @param idCroquis El id el croquis
	 */
	void deleteCroquis(Long idEscuela, Long idSector, Long idCroquis) throws NoEncontradoException;

	/**
	 * Añade nuevo trazo de vía al croquis
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idSector  El id del sector
	 * @param idCroquis El id del croquis
	 * @param idVia     El id de la vía
	 * @param trazoVia  El trazo de vía
	 * @return El trazo persistido
	 * @throws ServiceException 
	 */
	TrazoVia addTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia, 
			TrazoVia trazoVia) throws ServiceException;

	/**
	 * Actualiza el trazo de vía pasado
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idSector  El id del sector
	 * @param idCroquis El id del croquis
	 * @param idVia     El id de la vía
	 * @param trazoVia  El trazo de vía
	 * @return El trazo actualizado
	 * @throws NoEncontradoException
	 */
	TrazoVia updateTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia,
			TrazoVia trazoVia) throws NoEncontradoException;

	/**
	 * Borra el trazo de vía cuya id es pasada
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idSector El id del sector
	 * @param idCroquis El id del croquis
	 * @param idVia El id de la vía
	 * @throws NoEncontradoException
	 */
	void deleteTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia) throws NoEncontradoException;

	/**
	 * Persiste nuevo cierre de temporada para la escuela pasada
	 * 
	 * @param idEscuela       La id de la escuela
	 * @param cierreTemporada El nuevo cierre de temporada
	 * @return El cierre de temporada persistido
	 * @throws ServiceException
	 */
	CierreTemporada addCierreTemporada(Long idEscuela, CierreTemporada cierreTemporada) throws ServiceException;

	/**
	 * Borra el cierre de temporada cuya id es pasada
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idCierre  El id del cierre
	 */
	void deleteCierre(Long idEscuela, Long idCierre) throws ServiceException;

}
