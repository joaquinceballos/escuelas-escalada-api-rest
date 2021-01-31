package es.uniovi.service;

import es.uniovi.exception.ImagenNoValidaException;

public interface ImagenService {

	/**
	 * Decodifica String codificado en Base64 a array de byte
	 * 
	 * @param base64 El String a decodificar
	 * @return Array de byte decoficado
	 * @throws ImagenNoValidaException Si el String no es un Base64 válido
	 */
	byte[] toBytes(String base64) throws ImagenNoValidaException;

	/**
	 * Codifica array de byte en String Base64
	 * 
	 * @param bytes El array de byte
	 * @return El String codificado en Base64
	 * @throws ImagenNoValidaException
	 */
	String toBase64(byte[] bytes) throws ImagenNoValidaException;

	/**
	 * Comprueba si la imagen es válida
	 * 
	 * @param bytes El array de byte
	 * @throws ImagenNoValidaException Si el tamaño o formato no es correcto
	 */
	void checkImagen(byte[] bytes) throws ImagenNoValidaException;

	/**
	 * Comprueba si la imagen es válida
	 * 
	 * @param base64 Imágen codificada en Base64
	 * @throws ImagenNoValidaException Si el tamaño o formato no es correcto
	 */
	void checkImagen(String base64) throws ImagenNoValidaException;

}
