package es.uniovi.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ViaDto {
	
	private Long id;

	@NotBlank
	private String nombre;

	@NotBlank
	private String grado; // TODO se debé validar el grado??

	@Min(0)
	@JsonInclude(Include.NON_NULL)
	private Integer numeroChapas;

	@Min(0)
	@JsonInclude(Include.NON_NULL)
	private Double longitud;
	
	@Size(max = 2000)
	private String informacion;

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

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public Integer getNumeroChapas() {
		return numeroChapas;
	}

	public void setNumeroChapas(Integer numeroChapas) {
		this.numeroChapas = numeroChapas;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	@Override
	public String toString() {
		return "ViaDto [id=" + id + ", nombre=" + nombre + ", grado=" + grado + ", numeroChapas=" + numeroChapas
				+ ", longitud=" + longitud + "]";
	}

}
