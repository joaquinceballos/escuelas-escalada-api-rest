package es.uniovi.repository;

import java.util.Optional;

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

	/**
	 * Obtiene el usuario cuyo email es pasado
	 * 
	 * @param email El email
	 * @return El usuario recuperado
	 */
	Optional<Usuario> findByEmail(String email);

}
