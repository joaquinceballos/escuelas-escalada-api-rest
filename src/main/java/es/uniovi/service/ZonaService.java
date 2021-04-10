package es.uniovi.service;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Zona;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;

public interface ZonaService {

	/**
	 * Obtine la página de zonas para los parámetros pasados
	 * 
	 * @param page El número de página
	 * @param size El tamaño de la página
	 * @param pais El país por el que se filtrarán los resultados
	 * @return La página de resultados
	 */
	Page<Zona> getZonas(Integer page, Integer size, String pais);

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
