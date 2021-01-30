package es.uniovi.service;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.domain.Page;

import es.uniovi.domain.Ascension;
import es.uniovi.domain.Escuela;
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

	/**
	 * Obtiene la lista paginada de ascensiones del usuario
	 * 
	 * @param idUsuario El id del usuario
	 * @param page      el número de página
	 * @param size      El tamaño de página
	 * @return La página
	 * @throws NoEncontradoException
	 */
	Page<Ascension> getAscensiones(Long idUsuario, Integer page, Integer size) throws NoEncontradoException;

	/**
	 * Registra nueva ascensión para el usuario
	 * 
	 * @param ascension La ascensión
	 * @param idUsuario La id del usuario
	 * @param idVia     La id de la vía
	 * @return La ascensión persistida
	 * @throws NoEncontradoException
	 */
	Ascension addAscension(Long idUsuario, Long idVia, Ascension ascension) throws NoEncontradoException;

	Ascension updateAscension(Long idUsuario, Long idVia, Ascension entity) throws NoEncontradoException;

}
