package es.uniovi.dto;

import javax.validation.constraints.NotBlank;

public class EscuelaDto {

	private Long id;

	@NotBlank
	private String nombre;

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

	@Override
	public String toString() {
		return "EscuelaDto [id=" + id + ", nombre=" + nombre + "]";
	}

}
