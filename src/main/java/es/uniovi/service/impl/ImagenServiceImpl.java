package es.uniovi.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.uniovi.exception.ImagenNoValidaException;
import es.uniovi.service.ImagenService;

@Service
public class ImagenServiceImpl implements ImagenService {
	
    private static final Logger logger = LogManager.getLogger(ImagenServiceImpl.class);
	
	@Value("${imagen.tipos-validos}")
	private List<String> tiposImagenValidos;
	
	@Value("${imagen.max-size}")
	private Integer maxTamano;

	@Override
	public byte[] toBytes(String base64) throws ImagenNoValidaException {
		try {
			return Base64.getDecoder().decode(base64);
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
			logger.debug(e);
			throw new ImagenNoValidaException();
		}
	}

	private void check(byte[] bytes) throws ImagenNoValidaException {
		checkFormato(bytes);
		checkTamano(bytes);
	}

	private void checkTamano(byte[] bytes) throws ImagenNoValidaException {
		if (bytes.length > maxTamano) {
			throw new ImagenNoValidaException();
		}
	}

	private String checkFormato(byte[] bytes) throws ImagenNoValidaException {
		int n = bytes.length;
		byte[] primerosBytes = Arrays.copyOf(bytes, Math.min(n, 2000));
		String tipo = new Tika().detect(primerosBytes);
		if(!tiposImagenValidos.contains(tipo)) {
			throw new ImagenNoValidaException();
		}
		return tipo;
	}

	@Override
	public String toBase64(byte[] bytes) throws ImagenNoValidaException {
		return Base64.getEncoder().encodeToString(bytes);
	}

	@Override
	public void checkImagen(byte[] bytes) throws ImagenNoValidaException {
		check(bytes);
	}

	@Override
	public void checkImagen(String base64) throws ImagenNoValidaException {
		check(toBytes(base64));
	}

	@Override
	public String getFormatoImagen(String base64) throws ImagenNoValidaException {
		return checkFormato(toBytes(base64));
	}

}
