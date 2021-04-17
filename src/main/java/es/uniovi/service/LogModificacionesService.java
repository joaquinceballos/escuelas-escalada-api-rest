package es.uniovi.service;

import es.uniovi.domain.RecursoLogeable;

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

}
