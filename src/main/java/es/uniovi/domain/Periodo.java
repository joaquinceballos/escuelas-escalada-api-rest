package es.uniovi.domain;

import java.time.temporal.Temporal;

public interface Periodo<T extends Temporal> {

	T getInicio();

	T getFin();
	
}
