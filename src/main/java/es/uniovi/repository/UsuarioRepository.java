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

	/**
	 * Obtiene el usuario cuyo username es pasado
	 * 
	 * @param username El nombre de usuario
	 * @return El usuario recuperado
	 */
	Optional<Usuario> findByUsername(String username);

	/**
	 * Comprueba si ya existe un usuario con el email pasado
	 * 
	 * @param email El email
	 * @return True, si existe
	 */
	boolean existsByEmail(String email);

	/**
	 * Compruba si ya existe un usuario con el nombre de usuario pasado
	 * 
	 * @param username El nombre de usuario
	 * @return True, si existe
	 */
	boolean existsByUsername(String username);

}
