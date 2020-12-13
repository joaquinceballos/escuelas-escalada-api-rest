package es.uniovi.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Escuela;
import es.uniovi.domain.Sector;

@Repository
public interface SectorRepository extends CrudRepository<Sector, Long>{

	Set<Sector> findByEscuela(Escuela escuela);

}
