package es.uniovi.service.impl;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.uniovi.controller.BaseController;
import es.uniovi.domain.LogModificaciones;
import es.uniovi.domain.LogModificaciones.AccionLog;
import es.uniovi.domain.RecursoLogeable;
import es.uniovi.domain.Usuario;
import es.uniovi.repository.LogModificacionesRepository;
import es.uniovi.repository.UsuarioRepository;
import es.uniovi.service.LogModificacionesService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class LogModificacionesServiceImpl implements LogModificacionesService {
	
    private static final Logger logger = LogManager.getLogger(BaseController.class);

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

	private Usuario getUsuarioAutenticado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
			logger.error("Error serializando recurso logeable: {}",  e.getMessage());
			logger.debug(e);
		}
		return log;
	}
}
