package es.uniovi.exception;

public class NoEncontradoException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9206878460371510068L;

	private final String recurso;
	private final Object valor;
	
	public NoEncontradoException(String recurso, Object valor) {
		this.recurso = recurso;
		this.valor = valor;
	}

	public String getRecurso() {
		return recurso;
	}

	public Object getValor() {
		return valor;
	}

}
