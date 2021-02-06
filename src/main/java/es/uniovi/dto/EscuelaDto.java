package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class EscuelaDto {

	private Long id;

	@NotBlank
	private String nombre;

	@JsonInclude(Include.NON_NULL)
	private List<SectorDto> sectores = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<SectorDto> getSectores() {
		return sectores;
	}

	public void setSectores(List<SectorDto> sectores) {
		this.sectores = sectores;
	}

	@Override
	public String toString() {
		return "EscuelaDto [id=" + id + ", nombre=" + nombre + ", sectores=" + sectores + "]";
	}

}
