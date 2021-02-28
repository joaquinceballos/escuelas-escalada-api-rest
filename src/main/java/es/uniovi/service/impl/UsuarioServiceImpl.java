package es.uniovi.service.impl;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Ascension;
import es.uniovi.domain.Rol;
import es.uniovi.domain.Usuario;
import es.uniovi.domain.Via;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.AscensionRepository;
import es.uniovi.repository.RolRepository;
import es.uniovi.repository.UsuarioRepository;
import es.uniovi.repository.ViaRepository;
import es.uniovi.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private AscensionRepository ascensionRepository;
	
	@Autowired
	private ViaRepository viaRepository;

	@Override
	public Page<Usuario> getUsuarios(Integer page, Integer size) {
		return usuarioRepository.findAll(PageRequest.of(page, size, Sort.by("nombre")));
	}

	@Override
	public Usuario addUsuario(Usuario usuario) throws RestriccionDatosException {
		try {
			codificaPassword(usuario);
			Rol rolUser = rolRepository.findByNombre("USER").orElseThrow(()-> new NoSuchElementException("rol USER"));
			usuario.setRoles(Arrays.asList(rolUser));
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
	public void deleteUsuario(Long idUsuario) throws NoEncontradoException {
		usuarioRepository.delete(doGetUsuario(idUsuario));
	}

	@Override
	public Page<Ascension> getAscensiones(Long idUsuario, Integer page, Integer size) throws NoEncontradoException  {
		return ascensionRepository.findByUsuario(doGetUsuario(idUsuario), PageRequest.of(page, size));
	}

	@Override
	public Ascension addAscension(Long idUsuario, Long idVia, Ascension ascension) throws NoEncontradoException {
		ascension.setUsuario(doGetUsuario(idUsuario));
		ascension.setVia(doGetVia(idVia));
		return ascensionRepository.save(ascension);
	}

	@Override
	public Ascension updateAscension(Long idUsuario, Long idVia, Ascension actualizada) throws NoEncontradoException {
		Usuario usuario = doGetUsuario(idUsuario);
		Via via = doGetVia(idVia);
		Ascension ascension = ascensionRepository.findById(actualizada.getId())
				.orElseThrow(() -> new NoEncontradoException("ascension.id", actualizada.getId()));
		if (usuario.equals(ascension.getUsuario()) && via.equals(ascension.getVia())) {
			actualizada.setUsuario(usuario);
			actualizada.setVia(via);
			return ascensionRepository.save(actualizada);
		}
		throw new NoEncontradoException(
				"usuario/ascension/via",
				usuario.getId() + "/" + actualizada.getId() + "/" + via.getId());
	}

	private Usuario doGetUsuario(Long id) throws NoEncontradoException {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NoEncontradoException("usuario.id", id));
		usuario.setPassword(null);
		return usuario;
	}

	private void codificaPassword(Usuario usuario) {
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
	}

	private Via doGetVia(Long idVia) throws NoEncontradoException {
		return viaRepository.findById(idVia).orElseThrow(() -> new NoEncontradoException("via.id", idVia));
	}

	@Override
	public Usuario getUsuario(String username) throws NoAutorizadoException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		if (username == null || !username.equals(currentPrincipalName)) {
			throw new NoAutorizadoException();
		}
		return usuarioRepository.findByUsername(username).orElse(null);
	}

}
