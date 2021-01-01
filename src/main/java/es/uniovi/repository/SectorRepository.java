package es.uniovi.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {

	/**
	 * Recupera de BDD los sectores de la escuela pasada
	 * 
	 * @param escuela La escuela
	 * @return el Set se sectores de la escuela
	 */
	Set<Sector> findByEscuela(Escuela escuela);

}
