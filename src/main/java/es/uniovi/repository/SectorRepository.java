package es.uniovi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;

public interface SectorRepository extends CrudRepository<Sector, Long>{

	List<Sector> findByEscuela(Escuela escuela);

}
