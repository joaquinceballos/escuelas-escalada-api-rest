package es.uniovi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.Ascension;
import es.uniovi.domain.Usuario;
import es.uniovi.domain.Via;

public interface AscensionRepository extends CrudRepository<Ascension, Long> {

	Page<Ascension> findByUsuario(Usuario usuario, Pageable pageable);

	Page<Ascension> findByViaOrderByIdDesc(Via via, Pageable pageable);

}
