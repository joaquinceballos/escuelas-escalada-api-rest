package es.uniovi.exception;

public abstract class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6053411947493627367L;

	protected ServiceException() {}

	protected ServiceException(String mensaje) {
		super(mensaje);
	}

}
