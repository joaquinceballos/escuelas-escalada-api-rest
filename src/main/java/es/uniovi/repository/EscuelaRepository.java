package es.uniovi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Escuela;

@Repository
public interface EscuelaRepository extends CrudRepository<Escuela, Long> {

}
