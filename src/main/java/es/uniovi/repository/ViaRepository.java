package es.uniovi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Sector;
import es.uniovi.domain.Via;

@Repository
public interface ViaRepository extends JpaRepository<Via, Long> {

	List<Via> findAllBySector(Sector sector);

	/**
	 * Comprueba si ya existe una vía con el nombre pasado en el sector pasado
	 * 
	 * @param sector El Sector
	 * @param nombre El nombre de la vía a checkear
	 * @return True si ya existe
	 */
	boolean existsBySectorAndNombre(Sector sector, String nombre);

}
