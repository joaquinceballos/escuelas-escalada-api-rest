package es.uniovi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.uniovi.domain.Zona;
import es.uniovi.exception.NoEncontradoException;
import es.uniovi.exception.RestriccionDatosException;
import es.uniovi.exception.ServiceException;
import es.uniovi.repository.ZonaRepository;
import es.uniovi.service.ZonaService;

@Service
public class ZonaServiceImpl implements ZonaService {

	@Autowired
	private ZonaRepository zonaRepository;

	@Override
	public Page<Zona> getZonas(Integer page, Integer size, String pais) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by("numeroEscuelas").descending());
		if (pais != null) {
			return zonaRepository.findAllByPais(pageable, pais);
		} else {
			return zonaRepository.findAll(pageable);
		}
	}

	@Override
	public Zona addZona(Zona zona) throws RestriccionDatosException {
		if (zonaRepository.existsByPaisAndRegion(zona.getPais(), zona.getRegion())) {
			throw new RestriccionDatosException("Zona ya existe");
		}
		return zonaRepository.save(zona);
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
		return zonaRepository.save(persistida);
	}

	@Override
	public void deleteZona(Long id) throws ServiceException {
		Zona zona = doGetZona(id);
		if (zonaRepository.countEscuelasById(zona.getId()) > 0) {
			throw new RestriccionDatosException("No es posible borrar la zona");
		}
		zonaRepository.delete(zona);
	}

}
