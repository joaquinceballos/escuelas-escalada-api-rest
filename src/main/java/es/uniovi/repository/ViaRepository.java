package es.uniovi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uniovi.domain.Via;

@Repository
public interface ViaRepository extends CrudRepository<Via, Long> {

}
