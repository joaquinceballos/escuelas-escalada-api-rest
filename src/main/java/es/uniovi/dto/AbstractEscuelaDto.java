package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.uniovi.domain.Ubicable;
import es.uniovi.validation.Coordenadas;

@Coordenadas
public abstract class AbstractEscuelaDto implements Ubicable {

	private Long id;

	@NotBlank
	private String nombre;

	@JsonInclude(Include.NON_NULL)
	private Double latitud;

	@JsonInclude(Include.NON_NULL)
	private Double longitud;

	@Size(max = 5000)
	@JsonInclude(Include.NON_NULL)
	private String informacion;

	@Valid
	private List<CierreTemporadaDto> cierresTemporada = new ArrayList<>();
	
	private ZonaDto zona;

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
	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	@Override
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

	public List<CierreTemporadaDto> getCierresTemporada() {
		return cierresTemporada;
	}

	public void setCierresTemporada(List<CierreTemporadaDto> cierresTemporada) {
		this.cierresTemporada = cierresTemporada;
	}

	public ZonaDto getZona() {
		return zona;
	}

	public void setZona(ZonaDto zona) {
		this.zona = zona;
	}

	@Override
	public String toString() {
		return "EscuelaDto [id=" + id + ", nombre=" + nombre + "]";
	}

}
