package es.uniovi.common;

public abstract class Constantes {

	public static final Integer ERROR_INTERNO = 99;

	////// Constantes de seguridad //////

	/**
	 * Tiempo en milisegundos a partir del cual caducarán los tokens generados
	 */
	public static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hora

	/**
	 * Prefijo "Bearer " en los tokens generados
	 */
	public static final String TOKEN_PREFIX = "Bearer ";

	/**
	 * Nombre cabecera autenticación
	 */
	public static final String HEADER_STRING = "Authorization";

	/**
	 * Endpoint del login
	 */
	public static final String SIGN_UP_URL = "/login";
	
	public static final String ROLE_ADMIN = "ADMIN";

	public static final String ROLE_USER = "USER";
	
	private Constantes() {}
}
