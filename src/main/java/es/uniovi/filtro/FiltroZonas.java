package es.uniovi.filtro;

import es.uniovi.validation.PaisIso;

public class FiltroZonas {

	@PaisIso
	private String pais;

	private Boolean conEscuelas;

	private Boolean todas;

	public FiltroZonas() {
		super();
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Boolean getConEscuelas() {
		return conEscuelas;
	}

	public void setConEscuelas(Boolean conEscuelas) {
		this.conEscuelas = conEscuelas;
	}

	public Boolean getTodas() {
		return todas;
	}

	public void setTodas(Boolean todas) {
		this.todas = todas;
	}

}
