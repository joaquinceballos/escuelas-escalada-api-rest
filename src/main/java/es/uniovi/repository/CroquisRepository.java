package es.uniovi.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.Croquis;
import es.uniovi.domain.Sector;

public interface CroquisRepository extends CrudRepository<Croquis, Long> {

	List<Croquis> findBySector(Sector sector);

}
