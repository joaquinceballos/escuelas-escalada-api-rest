package es.uniovi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Zona;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.filtro.FiltroZonas;
import es.uniovi.repository.ZonaRepository;
import es.uniovi.service.LogModificacionesService;
import es.uniovi.service.ZonaService;

@Service
public class ZonaServiceImpl implements ZonaService {

	@Autowired
	private ZonaRepository zonaRepository;
	
	@Autowired
	private LogModificacionesService logModificacionesService;

	@Override
	public Page<Zona> getZonas(Pageable pageable, FiltroZonas filtro) {
		if (filtro.getPais() != null) {
			if (Boolean.TRUE.equals(filtro.getConEscuelas())) {
				return zonaRepository.findAllByPaisAndNumeroEscuelasGreaterThan(filtro.getPais(), 0, pageable);
			} else {
				return zonaRepository.findAllByPais(filtro.getPais(), pageable);
			}
		} else {
			if (Boolean.TRUE.equals(filtro.getConEscuelas())) {
				return zonaRepository.findByNumeroEscuelasGreaterThan(0, pageable);
			} else {
				return zonaRepository.findAll(pageable);
			}
		}
	}

	@Override
	public Zona addZona(Zona zona) throws RestriccionDatosException {
		if (zonaRepository.existsByPaisAndRegion(zona.getPais(), zona.getRegion())) {
			throw new RestriccionDatosException("Zona ya existe");
		}
		Zona savedZona = zonaRepository.save(zona);
		logModificacionesService.logCrear(zona);
		return savedZona;
	}

	@Override
	public Zona getZona(Long id) throws NoEncontradoException {
		return doGetZona(id);
	}

	private Zona doGetZona(Long id) throws NoEncontradoException {
		return zonaRepository.findById(id).orElseThrow(() -> new NoEncontradoException("zona", id));
	}

	@Override
	public Zona actualizaZona(Long id, Zona zona) throws ServiceException {
		if (zonaRepository.existsByPaisAndRegion(zona.getPais(), zona.getRegion())) {
			throw new RestriccionDatosException("Zona ya existe");
		}
		Zona persistida = doGetZona(id);
		persistida.setPais(zona.getPais());
		persistida.setRegion(zona.getRegion());
		logModificacionesService.logActualizar(persistida);
		return zonaRepository.save(persistida);
	}

	@Override
	public void deleteZona(Long id) throws ServiceException {
		Zona zona = doGetZona(id);
		if (zonaRepository.countEscuelasById(zona.getId()) > 0) {
			throw new RestriccionDatosException("No es posible borrar la zona");
		}
		logModificacionesService.logBorrar(zona);
		zonaRepository.delete(zona);
	}

}
