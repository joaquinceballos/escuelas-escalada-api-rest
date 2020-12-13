package es.uniovi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Escuela;

@Repository
public interface EscuelaRepository extends CrudRepository<Escuela, Long> {

	Page<Escuela> findAll(Pageable pageable);

}
