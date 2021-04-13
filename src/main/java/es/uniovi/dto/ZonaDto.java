package es.uniovi.dto;

import javax.validation.constraints.NotEmpty;

import es.uniovi.validation.PaisIso;

public class ZonaDto {

	private Long id;

	@PaisIso
	private String pais;

	@NotEmpty
	private String region;
	
	private String informacion;

	private Integer numeroEscuelas;

	private Integer numeroVias;

	public ZonaDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getNumeroEscuelas() {
		return numeroEscuelas;
	}

	public void setNumeroEscuelas(Integer numeroEscuelas) {
		this.numeroEscuelas = numeroEscuelas;
	}

	public Integer getNumeroVias() {
		return numeroVias;
	}

	public void setNumeroVias(Integer numeroVias) {
		this.numeroVias = numeroVias;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	@Override
	public String toString() {
		return "ZonaDto [id=" + id + ", pais=" + pais + ", region=" + region + ", numeroEscuelas=" + numeroEscuelas
				+ ", numeroVias=" + numeroVias + "]";
	}

}
