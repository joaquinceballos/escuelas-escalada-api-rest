package es.uniovi.service;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.fge.jsonpatch.JsonPatch;

import es.uniovi.domain.Ascension;
import es.uniovi.domain.CierreTemporada;
import es.uniovi.domain.Croquis;
import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.TrazoVia;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.ServiceException;

public interface EscuelaService {

	/**
	 * Obtine la página de escuelas para los parámetros pasados
	 * 
	 * @param page   El número de página
	 * @param size   El tamaño de la página
	 * @param idZona La zona por la que filtrar los resultados
	 * @return La página de resultados
	 * @throws NoEncontradoException No se encuentra la zona
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	Page<Escuela> getEscuelas(
			Integer page,
			Integer size,
			Long idZona) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Obtiene la Escuela cuyo id es pasado
	 * 
	 * @param id La id
	 * @return La Escuela buscada
	 * @throws NoEncontradoException Si no se han encontrado resultados
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	Escuela getEscuela(Long id) throws NoEncontradoException, NoAutorizadoException;

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
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	Set<Sector> getSectores(Long id) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Obtiene el sector cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector a buscar
	 * @return El sector
	 * @throws NoEncontradoException Si la id de escuela no existe o si el id de
	 *                               sector no pertenece a la escuela pasada
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	Sector getSector(Long idEscuela, Long idSector) throws NoEncontradoException, NoAutorizadoException;

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
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 * @throws ServiceException
	 */
	Set<Via> getVias(Long idEscuela, Long idSector) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Obtiene la vía cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @return La vía
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	Via getVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException, NoAutorizadoException;

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
	 * @throws NoAutorizadoException 
	 */
	void deleteEscuela(Long id) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Borra el sector cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException 
	 */
	void deleteSector(Long idEscuela, Long idSector) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Borra la vía cuya id es pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException 
	 */
	void deleteVia(Long idEscuela, Long idSector, Long idVia) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Actualiza todos los campos simples de la escuela cuyo id es pasado
	 * 
	 * @param id      La id de la escuela
	 * @param escuela La escuela
	 * @return La escuela actualizada
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException 
	 */
	Escuela actualizaEscuela(Long id, Escuela escuela) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Actualiza todos los campos simples del sector cuyo id es pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param sector    El sector con los datos a actualizar
	 * @return El sector actualizado
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException 
	 */
	Sector actualizaSector(Long idEscuela, Long idSector, Sector sector) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Actualiza todos los campos simples de la escuela pasada
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @param via       La vía con los campos a actualizar
	 * @return La vía actualizada
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException 
	 */
	Via actualizaVia(Long idEscuela, Long idSector, Long idVia, Via via) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Obtiene los croquis del sector pasado
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @return La lista de croquis del sector
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	List<Croquis> getCroquis(Long idEscuela, Long idSector) throws NoEncontradoException, NoAutorizadoException;
	
	/**
	 * Obtiene el croquis pasado
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idSector  El id del sector
	 * @param idCroquis El id del croquis
	 * @return El croquis
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException Usuario sin permisos suficientes
	 */
	Croquis getCroquis(Long idEscuela, Long idSector, Long idCroquis) throws NoEncontradoException, NoAutorizadoException;

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
	 * @throws NoAutorizadoException 
	 */
	void deleteCroquis(
			Long idEscuela,
			Long idSector,
			Long idCroquis) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Actualiza el tipo de leyendda para el croquis pasado
	 * 
	 * @param idEscuela   La id de la escuela
	 * @param idSector    La id del sector
	 * @param idCroquis   La id del croquis
	 * @param tipoLeyenda El tipo de leyenda
	 * @throws NoAutorizadoException 
	 * @throws NoEncontradoException 
	 */
	void actualizaTipoLeyenda(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			String tipoLeyenda) throws NoAutorizadoException, NoEncontradoException;

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
	 * @throws NoAutorizadoException 
	 */
	TrazoVia updateTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia,
			TrazoVia trazoVia) throws NoEncontradoException, NoAutorizadoException;

	/**
	 * Borra el trazo de vía cuya id es pasada
	 * 
	 * @param idEscuela El id de la escuela
	 * @param idSector El id del sector
	 * @param idCroquis El id del croquis
	 * @param idVia El id de la vía
	 * @throws NoEncontradoException
	 * @throws NoAutorizadoException 
	 */
	void deleteTrazoVia(
			Long idEscuela,
			Long idSector,
			Long idCroquis,
			Long idVia) throws NoEncontradoException, NoAutorizadoException;

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

	/**
	 * Obtiene la página de ascensiones de la vía pasada.
	 * 
	 * @param idEscuela La id de la escuela
	 * @param idSector  La id del sector
	 * @param idVia     La id de la vía
	 * @param pageable  Los parámetros de paginación
	 * @return Las ascenciones
	 * @throws NoEncontradoException 
	 */
	Page<Ascension> getAscencionesVia(
			Long idEscuela,
			Long idSector,
			Long idVia,
			Pageable pageable) throws NoEncontradoException;

}
