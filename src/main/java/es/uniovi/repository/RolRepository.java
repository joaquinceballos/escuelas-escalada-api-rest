package es.uniovi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import es.uniovi.domain.NombreRol;
import es.uniovi.domain.Rol;

public interface RolRepository extends CrudRepository<Rol, Long> {

	/**
	 * Obtiene el rol cuyo nombre es pasado
	 * 
	 * @param nombre El nombre del rol
	 * @return El Rol
	 */
	Optional<Rol> findByNombre(NombreRol nombre);

}
