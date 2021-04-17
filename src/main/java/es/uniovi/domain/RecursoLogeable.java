package es.uniovi.domain;

import es.uniovi.domain.LogModificaciones.TipoRecurso;

public interface RecursoLogeable {

	/**
	 * Idntificador del recurso logeable
	 * 
	 * @return La id
	 */
	Long getId();

	/**
	 * Tipo de recurso logeable
	 * 
	 * @return El tipo es.cex.domain.LogModificaciones.TipoResurso
	 */
	TipoRecurso getTipo();

	/**
	 * @return El path del recurso
	 */
	String pathLog();

	/**
	 * Retorna la clase en que se debe serializar el recurso logeable, generalmente
	 * un DTO sin dependencias circulares
	 * 
	 * @return La clase para serializar
	 */
	Class<?> claseSerializar();

}
