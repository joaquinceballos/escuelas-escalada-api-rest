package es.uniovi.dto;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.uniovi.domain.Ubicable;
import es.uniovi.validation.Coordenadas;

@Coordenadas
public class SectorDto implements Ubicable {

	private Long id;

	@NotBlank
	private String nombre;

	@JsonInclude(Include.NON_NULL)
	private Double latitud;

	@JsonInclude(Include.NON_NULL)
	private Double longitud;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getLatitud() {
		return latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public String getNombre() {
		return nombre;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "SectorDto [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud + "]";
	}
}
