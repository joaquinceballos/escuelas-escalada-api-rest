package es.uniovi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.uniovi.domain.Zona;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.filtro.FiltroZonas;

public interface ZonaService {

	/**
	 * Obtine la página de zonas para los parámetros pasados
	 * 
	 * @param pageable Los parámetros de paginación
	 * @param filtro   El filtro
	 * @return La página de resultados
	 */
	Page<Zona> getZonas(Pageable pageable, FiltroZonas filtro);

	/**
	 * Persiste una nueva Zona
	 * 
	 * @param zona La zona
	 * @return La zona persistida
	 * @throws RestriccionDatosException
	 */
	Zona addZona(Zona zona) throws RestriccionDatosException;

	/**
	 * Obtiene la zona cuya id es pasada
	 * 
	 * @param id La id
	 * @return La zona
	 * @throws NoEncontradoException
	 */
	Zona getZona(Long id) throws NoEncontradoException;

	/**
	 * Actualiza la zona pasada
	 * 
	 * @param id   La id
	 * @param zona La zona con los nuevos campos
	 * @return La zona actualizada
	 * @throws ServiceException
	 */
	Zona actualizaZona(Long id, Zona zona) throws ServiceException;

	/**
	 * Borra la zona cuya id es pasada
	 * 
	 * @param id La id
	 * @throws ServiceException
	 */
	void deleteZona(Long id) throws ServiceException;

}
