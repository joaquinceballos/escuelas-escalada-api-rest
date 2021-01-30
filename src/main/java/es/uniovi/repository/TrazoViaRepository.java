package es.uniovi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.Croquis;
import es.uniovi.domain.TrazoVia;
import es.uniovi.domain.Via;

public interface TrazoViaRepository extends CrudRepository<TrazoVia, Long> {

	Optional<TrazoVia> findByCroquisAndVia(Croquis croquis, Via via);

}
