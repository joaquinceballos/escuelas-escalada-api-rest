package es.uniovi.exception;

public class RecursoYaExisteException extends RestriccionDatosException {

	private static final long serialVersionUID = 1L;

	private final String recurso;

	public RecursoYaExisteException(String recurso) {
		super("Recurso ya existe: " + recurso);
		this.recurso = recurso;
	}

	public String getRecurso() {
		return recurso;
	}	

}
