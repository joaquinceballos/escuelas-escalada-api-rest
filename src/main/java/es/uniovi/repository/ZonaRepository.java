package es.uniovi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import es.uniovi.domain.Zona;

public interface ZonaRepository extends PagingAndSortingRepository<Zona, Long> {

	/**
	 * Comprueba si existe una zona con los valores pasados
	 * 
	 * @param pais   El país
	 * @param region La región
	 * @return Si existe ya
	 */
	boolean existsByPaisAndRegion(String pais, String region);

	/**
	 * Comrpueba el número de escuelas que tienen asociada la zona
	 * 
	 * @param id La id de la zona
	 * @return El número de escuelas en la zona
	 */
	@Query("select count (e.id) from zona z, escuela e where z.id = e.zona and z.id = :id")
	Integer countEscuelasById(Long id);

	/**
	 * Recupera las zonas filtrando por país
	 * 
	 * @param pageable Parámetros de paginación
	 * @param pais     El país
	 * @return Las zonas
	 */
	Page<Zona> findAllByPais(Pageable pageable, String pais);

}
