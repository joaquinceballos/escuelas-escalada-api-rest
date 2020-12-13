package es.uniovi.exception;

public abstract class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6053411947493627367L;

	public ServiceException() {}

	public ServiceException(String mensaje) {
		super(mensaje);
	}

}
