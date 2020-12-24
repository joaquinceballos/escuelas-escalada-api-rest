package es.uniovi.service;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Usuario;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;

public interface UsuarioService {

	/**
	 * Obtiene la lista paginada de usuarios de la base de datos
	 * 
	 * @param page el número de página
	 * @param size El tamaño de la página
	 * @return La página
	 */
	Page<Usuario> getUsuarios(Integer page, Integer size);

	/**
	 * Añade nuevo usuario
	 * 
	 * @param usuario El usuario a añadir
	 * @return El usuario añadido
	 * @throws RestriccionDatosException
	 */
	Usuario addUsuario(Usuario usuario) throws RestriccionDatosException;

	/**
	 * Actualiza usuario
	 * 
	 * @param usuario El usuario a actualizar
	 * @return El usuario actualizado
	 * @throws RestriccionDatosException
	 * @throws ServiceException
	 */
	Usuario updateUsuario(Usuario usuario) throws ServiceException;

	/**
	 * Borra el usuario cuya id es pasada
	 * 
	 * @param id La id del usuario a borrar
	 * @throws NoEncontradoException
	 */
	void deleteUsuario(Long id) throws NoEncontradoException;

}
