package es.uniovi.service;

import es.uniovi.exception.NoAutorizadoException;

public interface PrivilegioService {

	/**
	 * Checkea que el usuario autenticado tenga privilegio de escritura
	 * 
	 * @throws NoAutorizadoException
	 */
	void checkPrivilegioEscritura() throws NoAutorizadoException;

	/**
	 * Checkea que el usuario autenticado tenga privilegio de borrado
	 * 
	 * @throws NoAutorizadoException
	 */
	void checkPrivilegioBorrado() throws NoAutorizadoException;

}
