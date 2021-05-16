package es.uniovi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import es.uniovi.validation.PaisIso;

public class ZonaDto {

	private Long id;

	@PaisIso
	@NotBlank
	private String pais;

	@NotEmpty
	private String region;
	
	@Size(max = 2000)
	private String informacion;

	private Integer numeroEscuelas;

	private Integer numeroVias;
	
	private Boolean visible;
	
	private String enlaceImagen;

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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getEnlaceImagen() {
		return enlaceImagen;
	}

	public void setEnlaceImagen(String enlaceImagen) {
		this.enlaceImagen = enlaceImagen;
	}

	@Override
	public String toString() {
		return "ZonaDto [id=" + id + ", pais=" + pais + ", region=" + region + ", numeroEscuelas=" + numeroEscuelas
				+ ", numeroVias=" + numeroVias + "]";
	}

}
