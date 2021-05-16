package es.uniovi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.uniovi.domain.NombrePrivilegio;
import es.uniovi.domain.Privilegio;
import es.uniovi.domain.Rol;
import es.uniovi.domain.Usuario;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.service.PrivilegioService;
import es.uniovi.service.UsuarioService;

@Service
public class PrivilegioServiceImpl implements PrivilegioService {
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public Boolean checkPrivilegio(NombrePrivilegio nombre) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Usuario usuario = null;
		try {
			usuario = usuarioService.getUsuario(authentication.getName());
		} catch (NoAutorizadoException e) {
			return false;
		}
		if (usuario == null) {
			return false;
		}
		return usuario
				.getRoles()
				.stream()
				.map(Rol::getPrivilegios)
				.anyMatch(l -> l
						.stream()
						.map(Privilegio::getNombre)
						.anyMatch(nombre::equals));
	}

}
