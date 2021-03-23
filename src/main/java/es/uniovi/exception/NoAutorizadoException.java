package es.uniovi.exception;

public class NoAutorizadoException extends ServiceException {

	private static final long serialVersionUID = -1016515840504546650L;

	public NoAutorizadoException() {
		super();
	}

	public NoAutorizadoException(String string) {
		super(string);
	}

}
