package es.uniovi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.uniovi.domain.LogModificaciones;
import es.uniovi.domain.RecursoLogeable;
import es.uniovi.filtro.FiltroCambios;

public interface LogModificacionesService {

	/**
	 * Registra la creación de nuevo recurso
	 * 
	 * @param recursoLogeable
	 */
	void logCrear(RecursoLogeable recursoLogeable);

	/**
	 * Registra la actualización de un recurso
	 * 
	 * @param recursoLogeable
	 */
	void logActualizar(RecursoLogeable recursoLogeable);

	/**
	 * Registra el borrado de un recurso
	 * 
	 * @param recursoLogeable
	 */
	void logBorrar(RecursoLogeable recursoLogeable);

	/**
	 * Retorna los últimos cambios públicos en los datos
	 * 
	 * @param pageable Parámetros de paginación
	 * @param filtro   El filtro
	 * @return La página de resultados
	 */
	Page<LogModificaciones> getUltimosCambios(Pageable pageable, FiltroCambios filtro);

}
