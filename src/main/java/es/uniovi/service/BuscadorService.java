package es.uniovi.service;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Escuela;

public interface BuscadorService {

	/**
	 * Pagina de escuelas filtradas por nombre
	 * 
	 * @param nombre El nombre a buscar en el repositorio
	 * @param page   El número de página
	 * @param size   El tamaño de página
	 * @return La página de resultados obtenida
	 */
	Page<Escuela> getEscuelas(String nombre, Integer page, Integer size);

}
