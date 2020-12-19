package es.uniovi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	/**
	 * Recupera los usuarios de base de datos
	 * 
	 * @param pageable El objeto de paginación
	 * @return La página de usuarios
	 */
	Page<Usuario> findAll(Pageable pageable);

}
