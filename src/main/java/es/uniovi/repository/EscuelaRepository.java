package es.uniovi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Zona;

@Repository
public interface EscuelaRepository extends JpaRepository<Escuela, Long> {

	/**
	 * Comprueba si existe una escuela cuyo nombre es pasado
	 * 
	 * @param name El nombre a buscar
	 * @return True si existe una escuela con el nombre pasado
	 */
	boolean existsByNombre(String nombre);

	/**
	 * Recupera las escuelas filtrando por zona
	 * 
	 * @param pageable La información de paginación
	 * @param zona     La zona
	 * @return Las escuelas
	 */
	Page<Escuela> findAllByZona(Pageable pageable, Zona zona);

}
