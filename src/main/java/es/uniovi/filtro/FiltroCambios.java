package es.uniovi.filtro;

public class FiltroCambios {

	private Long idRecurso;

	private Long idUsuario;

	public FiltroCambios() {
		super();
	}

	public Long getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(Long idRecurso) {
		this.idRecurso = idRecurso;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Override
	public String toString() {
		return "FiltroCambios [idRecurso=" + idRecurso + ", idUsuario=" + idUsuario + "]";
	}

}
