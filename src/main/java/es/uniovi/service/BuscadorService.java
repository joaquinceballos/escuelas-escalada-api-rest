package es.uniovi.service;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;

public interface BuscadorService {

	/**
	 * Pagina de escuelas filtradas por nombre
	 * 
	 * @param texto El texto a buscar en el repositorio
	 * @param page  El número de página
	 * @param size  El tamaño de página
	 * @return La página de resultados obtenida
	 */
	Page<Escuela> buscaEscuelas(String texto, Integer page, Integer size);

	/**
	 * Pagina de sectores con resultado de búsqueda por texto
	 * 
	 * @param texto El texto a buscar
	 * @param page  El número de página
	 * @param size  El tamaño de página
	 * @return La página de resultado obtenida
	 */
	Page<Sector> buscaSectores(String texto, Integer page, Integer size);

	/**
	 * Pagina de vías con resultado de búsqueda por texto
	 * 
	 * @param texto El texto a buscar
	 * @param page  El número de página
	 * @param size  El tamaño de página
	 * @return La página de resultado obtenida
	 */
	Page<Via> buscaVias(String texto, Integer page, Integer size);

}
