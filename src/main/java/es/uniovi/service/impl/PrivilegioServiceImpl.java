package es.uniovi.service.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.uniovi.domain.NombrePrivilegio;
import es.uniovi.domain.NombreRol;
import es.uniovi.domain.Rol;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.repository.RolRepository;
import es.uniovi.service.PrivilegioService;

@Service
public class PrivilegioServiceImpl implements PrivilegioService {

	@Autowired
	private RolRepository rolRepository;

	@Override
	public void checkPrivilegioEscritura() throws NoAutorizadoException {
		checkPrivilegio(NombrePrivilegio.ESCRITURA);
	}

	@Override
	public void checkPrivilegioBorrado() throws NoAutorizadoException {
		checkPrivilegio(NombrePrivilegio.BORRADO);		
	}

	private void checkPrivilegio(NombrePrivilegio privilegio) throws NoAutorizadoException {
		for (GrantedAuthority grantedAuthority : getAuthorities()) {
			NombreRol nombreRol = NombreRol.valueOf(grantedAuthority.getAuthority().replace("ROLE_", ""));
			Rol rol = rolRepository.findByNombre(nombreRol).orElseThrow();
			if (rol.getPrivilegios().stream().anyMatch(p -> privilegio.equals(p.getNombre()))) {
				return;
			}
		}
		throw new NoAutorizadoException("Rol del usuario no permite grabar nuevos datos");
	}

	private Collection<? extends GrantedAuthority> getAuthorities() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	}

}
