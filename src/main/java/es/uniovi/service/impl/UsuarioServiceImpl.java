package es.uniovi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Usuario;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.UsuarioRepository;
import es.uniovi.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public Page<Usuario> getUsuarios(Integer page, Integer size) {
		return usuarioRepository.findAll(PageRequest.of(page, size, Sort.by("nombre")));
	}

	@Override
	public Usuario addUsuario(Usuario usuario) throws RestriccionDatosException {
		try {
			codificaPassword(usuario);
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public Usuario updateUsuario(Usuario usuario) throws ServiceException {
		try {
			if (!usuarioRepository.existsById(usuario.getId())) {
				throw new NoEncontradoException("usuario.id", usuario.getId());
			}
			codificaPassword(usuario);
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public void deleteUsuario(Long id) throws NoEncontradoException {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NoEncontradoException("usuario.id", id));
		usuarioRepository.delete(usuario);
	}

	private void codificaPassword(Usuario usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	}

}
