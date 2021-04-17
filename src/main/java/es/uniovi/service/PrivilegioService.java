package es.uniovi.service;

import es.uniovi.domain.NombrePrivilegio;

public interface PrivilegioService {

	/**
	 * Checkea si el usuario autenticado tiene el privilegio cuyo nombre es pasado
	 * 
	 * @param nombre El nombre del privilegio
	 * @return Si el usuario autenticado tiene el privilegio
	 */
	Boolean checkPrivilegio(NombrePrivilegio nombre);

}
