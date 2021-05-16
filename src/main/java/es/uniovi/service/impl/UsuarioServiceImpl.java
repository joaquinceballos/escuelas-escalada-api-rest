package es.uniovi.service.impl;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Ascension;
import es.uniovi.domain.LogModificaciones.AccionLog;
import es.uniovi.domain.NombreRol;
import es.uniovi.domain.RecursoLogeable;
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
import es.uniovi.service.LogModificacionesService;
import es.uniovi.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private static final Logger logger = LogManager.getLogger(UsuarioServiceImpl.class);

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
	
	@Autowired
	private LogModificacionesService logModificacionesService;

	@Override
	public Page<Usuario> getUsuarios(Integer page, Integer size) {
		return usuarioRepository.findAll(PageRequest.of(page, size, Sort.by("nombre")));
	}

	@Override
	public Usuario addUsuario(Usuario usuario) throws RestriccionDatosException {
		try {
			codificaPassword(usuario);
			Rol rolUser = rolRepository
					.findByNombre(NombreRol.valueOf("USER"))
					.orElseThrow(() -> new NoSuchElementException("rol USER"));
			usuario.setRoles(Arrays.asList(rolUser));
			Usuario savedUsuario = usuarioRepository.save(usuario);
			logModificaciones(savedUsuario, AccionLog.CREAR);
			return savedUsuario;
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
			logModificaciones(usuario, AccionLog.ACTUALIZAR);
			return usuarioRepository.save(usuario);
		} catch (DataIntegrityViolationException e) {
			throw new RestriccionDatosException(e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public void deleteUsuario(Long idUsuario) throws NoEncontradoException {
		Usuario usuario = doGetUsuario(idUsuario);
		logModificaciones(usuario, AccionLog.BORRAR);
		usuarioRepository.delete(usuario);
	}

	@Override
	public Page<Ascension> getAscensiones(Long idUsuario, Integer page, Integer size) throws NoEncontradoException  {
		return ascensionRepository.findByUsuario(doGetUsuario(idUsuario), PageRequest.of(page, size));
	}

	@Override
	public Ascension addAscension(Long idUsuario, Long idVia, Ascension ascension) throws NoEncontradoException {
		ascension.setUsuario(doGetUsuario(idUsuario));
		ascension.setVia(doGetVia(idVia));
		Ascension savedAscension = ascensionRepository.save(ascension);
		logModificaciones(savedAscension, AccionLog.CREAR);
		return savedAscension;
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
			logModificaciones(actualizada, AccionLog.ACTUALIZAR);
			return ascensionRepository.save(actualizada);
		}
		throw new NoEncontradoException(
				"usuario/ascension/via",
				usuario.getId() + "/" + actualizada.getId() + "/" + via.getId());
	}

	private Usuario doGetUsuario(Long id) throws NoEncontradoException {
		Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new NoEncontradoException("usuario.id", id));
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
	
	private void logModificaciones(RecursoLogeable logeable, AccionLog accionLog) {
		try {
			if (AccionLog.CREAR.equals(accionLog)) {
				logModificacionesService.logCrear(logeable);
			} else if (AccionLog.ACTUALIZAR.equals(accionLog)) {
				logModificacionesService.logActualizar(logeable);
			} else if (AccionLog.BORRAR.equals(accionLog)) {
				logModificacionesService.logBorrar(logeable);
			} else {
				throw new IllegalArgumentException("Accion de log no esperada: " + accionLog);
			}
		} catch (DataAccessException e) {
			logger.error("Error persistiendo Log modificaciones: {}", e.getMessage());
			logger.debug(e);
		}
	}
	
}
