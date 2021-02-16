package es.uniovi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Escuela;

@Repository
public interface EscuelaRepository extends JpaRepository<Escuela, Long> {

	/**
	 * Comprueba si existe una escuela cuyo nombre es pasado
	 * 
	 * @param name El nombre a buscar
	 * @return True si existe una escuela con el nombre pasado
	 */
	boolean existsByNombre(String nombre);

}
