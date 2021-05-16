package es.uniovi.service.impl;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Rol;
import es.uniovi.domain.Usuario;
import es.uniovi.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) {
		Usuario usuario;
		if (EmailValidator.getInstance().isValid(username)) {
			usuario = usuarioRepository.findByEmail(username).orElseThrow(noExiste(username));
		} else {
			usuario = usuarioRepository.findByUsername(username).orElseThrow(noExiste(username));
		}
		return User
				.withUsername(usuario.getUsername())
				.roles(usuario
					.getRoles()
					.stream()
					.map(Rol::getNombre)
					.map(Object::toString)
					.collect(Collectors.toList())
					.toArray(new String[0]))
				.password(usuario.getPassword())
				.build();
	}

	private Supplier<? extends UsernameNotFoundException> noExiste(String username) {
		return () -> new UsernameNotFoundException("No se encontró usuario: " + username);
	}
	
}
