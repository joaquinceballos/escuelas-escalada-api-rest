package es.uniovi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.Croquis;
import es.uniovi.domain.TrazoVia;
import es.uniovi.domain.Via;

public interface TrazoViaRepository extends CrudRepository<TrazoVia, Long> {

	/**
	 * Recupera el trazo de la vía pasada en el croquis pasado
	 * 
	 * @param croquis El Croquis
	 * @param via     La vía
	 * @return El trazo de la vía
	 */
	Optional<TrazoVia> findByCroquisAndVia(Croquis croquis, Via via);

	/**
	 * Comprueba si existe registro para el croquis y vía pasados
	 * 
	 * @param croquis El croquis
	 * @param via     Lá vía
	 * @return True si existe
	 */
	boolean existsByCroquisAndVia(Croquis croquis, Via via);

}
