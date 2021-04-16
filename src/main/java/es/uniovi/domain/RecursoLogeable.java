package es.uniovi.domain;

import es.uniovi.domain.LogModificaciones.TipoRecurso;

public interface RecursoLogeable {

	Long getId();
	
	TipoRecurso getTipo();
	
	String pathLog();
	
}
