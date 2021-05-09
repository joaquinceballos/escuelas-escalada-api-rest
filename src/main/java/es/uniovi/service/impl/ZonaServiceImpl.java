package es.uniovi.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.uniovi.domain.LogModificaciones.AccionLog;
import es.uniovi.domain.NombrePrivilegio;
import es.uniovi.domain.RecursoLogeable;
import es.uniovi.domain.Zona;
import es.uniovi.exception.NoAutorizadoException;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RecursoYaExisteException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.filtro.FiltroZonas;
import es.uniovi.repository.ZonaRepository;
import es.uniovi.service.LogModificacionesService;
import es.uniovi.service.PrivilegioService;
import es.uniovi.service.ZonaService;

@Service
public class ZonaServiceImpl implements ZonaService {
	
	private static final Logger logger = LogManager.getLogger(ZonaServiceImpl.class);

	@Autowired
	private ZonaRepository zonaRepository;
	
	@Autowired
	private LogModificacionesService logModificacionesService;
	
	@Autowired
	private PrivilegioService privilegioService;

	@Override
	public Page<Zona> getZonas(Pageable pageable, FiltroZonas filtro) throws NoAutorizadoException {
		checkPrivilegioLectura();
		if (filtro.getPais() != null) {
			if (Boolean.TRUE.equals(filtro.getConEscuelas())) {
				return zonaRepository.findAllByPaisAndNumeroEscuelasGreaterThanAndVisibleTrue(filtro.getPais(), 0, pageable);
			} else {
				return zonaRepository.findAllByPaisAndVisibleTrue(filtro.getPais(), pageable);
			}
		} else {
			if (Boolean.TRUE.equals(filtro.getConEscuelas())) {
				return zonaRepository.findByNumeroEscuelasGreaterThanAndVisibleTrue(0, pageable);
			} else {
				return zonaRepository.findAllByVisibleTrue(pageable);
			}
		}
	}

	@Override
	public Page<Zona> getTodasLasZonas(Pageable pageable) {
		return zonaRepository.findAll(pageable);
	}

	@Override
	public Zona addZona(Zona zona) throws RestriccionDatosException, NoAutorizadoException {
		checkPrivilegioEscritura();
		if (zonaRepository.existsByPaisAndRegion(zona.getPais(), zona.getRegion())) {
			throw new RecursoYaExisteException(zona.getPais() + "/" + zona.getRegion());
		}
		Zona savedZona = zonaRepository.save(zona);
		logModificaciones(zona, AccionLog.CREAR);
		return savedZona;
	}

	@Override
	public Zona getZona(Long id) throws NoEncontradoException, NoAutorizadoException {
		checkPrivilegioLectura();
		return doGetZona(id);
	}

	private Zona doGetZona(Long id) throws NoEncontradoException {
		return zonaRepository.findById(id).orElseThrow(() -> new NoEncontradoException("zona", id));
	}

	@Override
	public Zona actualizaZona(Long id, Zona zona) throws ServiceException {
		checkPrivilegioEscritura();
		if (zonaRepository.existsByPaisAndRegion(zona.getPais(), zona.getRegion())) {
			throw new RecursoYaExisteException(zona.getPais() + "/" + zona.getRegion());
		}
		Zona persistida = doGetZona(id);
		persistida.setPais(zona.getPais());
		persistida.setRegion(zona.getRegion());
		logModificaciones(persistida, AccionLog.ACTUALIZAR);
		return zonaRepository.save(persistida);
	}

	@Override
	public void deleteZona(Long id) throws ServiceException {
		checkPrivilegioBorrado();
		Zona zona = doGetZona(id);
		if (zonaRepository.countEscuelasById(zona.getId()) > 0) {
			throw new RestriccionDatosException("No es posible borrar la zona");
		}
		logModificaciones(zona, AccionLog.BORRAR);
		zonaRepository.delete(zona);
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

	private void checkPrivilegioLectura() throws NoAutorizadoException {
		checkPrivilegio(NombrePrivilegio.LECTURA);
	}

	private void checkPrivilegioEscritura() throws NoAutorizadoException {
		checkPrivilegio(NombrePrivilegio.ESCRITURA_ZONA);
	}

	private void checkPrivilegioBorrado() throws NoAutorizadoException {
		checkPrivilegio(NombrePrivilegio.BORRADO_ZONA);
	}

	private void checkPrivilegio(NombrePrivilegio nombre) throws NoAutorizadoException {
		if (Boolean.FALSE.equals(privilegioService.checkPrivilegio(nombre))) {
			throw new NoAutorizadoException("Usuario sin privilegios de " + nombre);
		}
	}

}
