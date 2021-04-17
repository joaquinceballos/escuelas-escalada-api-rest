package es.uniovi.service.impl;

import java.time.LocalDateTime;
import java.util.EnumSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.uniovi.domain.LogModificaciones;
import es.uniovi.domain.LogModificaciones.AccionLog;
import es.uniovi.domain.LogModificaciones.TipoRecurso;
import es.uniovi.domain.RecursoLogeable;
import es.uniovi.domain.Usuario;
import es.uniovi.filtro.FiltroCambios;
import es.uniovi.repository.LogModificacionesRepository;
import es.uniovi.repository.UsuarioRepository;
import es.uniovi.service.LogModificacionesService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = DataAccessException.class)
public class LogModificacionesServiceImpl implements LogModificacionesService {

	private static final Logger logger = LogManager.getLogger(LogModificacionesServiceImpl.class);

	@Autowired
	private LogModificacionesRepository repository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void logCrear(RecursoLogeable recursoLogeable) {
		repository.save(setDatosComun(recursoLogeable, AccionLog.CREAR));
	}

	@Override
	public void logActualizar(RecursoLogeable recursoLogeable) {
		repository.save(setDatosComun(recursoLogeable, AccionLog.ACTUALIZAR));
	}

	@Override
	public void logBorrar(RecursoLogeable recursoLogeable) {
		repository.save(setDatosComun(recursoLogeable, AccionLog.BORRAR));
	}

	@Override
	public Page<LogModificaciones> getUltimosCambios(Pageable pageable, FiltroCambios filtro) {
		EnumSet<TipoRecurso> tipos = EnumSet.of(
				TipoRecurso.ESCUELA,
				TipoRecurso.ASCENSION,
				TipoRecurso.COMENTARIO,
				TipoRecurso.CROQUIS,
				TipoRecurso.SECTOR,
				TipoRecurso.TRAZO_VIA,
				TipoRecurso.VIA,
				TipoRecurso.ZONA);
		return repository.findModificacionesPublicas(tipos, filtro.getIdRecurso(), filtro.getIdUsuario(), pageable);
	}

	private Usuario getUsuarioAutenticado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return usuarioRepository.findByUsername(authentication.getName()).orElse(null);
	}

	private LogModificaciones setDatosComun(RecursoLogeable recursoLogeable, AccionLog accionLog) {
		LogModificaciones log = new LogModificaciones();
		log.setAccionLog(accionLog);
		log.setFecha(LocalDateTime.now());
		log.setIdRecurso(recursoLogeable.getId());
		log.setPath(recursoLogeable.pathLog());
		log.setTipoRecurso(recursoLogeable.getTipo());
		log.setUsuario(getUsuarioAutenticado());
		try {
			Object object = new ModelMapper().map(recursoLogeable, recursoLogeable.claseSerializar());
			String json = new ObjectMapper().writeValueAsString(object);
			log.setValorRecurso(json);
		} catch (JsonProcessingException | MappingException | ConfigurationException e) {
			logger.error("Error serializando recurso logeable: {}", e.getMessage());
			logger.debug(e);
		}
		return log;
	}

}
