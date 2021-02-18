package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.uniovi.domain.Ubicable;
import es.uniovi.validation.Coordenadas;
import es.uniovi.validation.PaisIso;

@Coordenadas
public class EscuelaDto implements Ubicable {

	private Long id;

	@NotBlank
	private String nombre;

	@JsonInclude(Include.NON_NULL)
	private List<SectorDto> sectores = new ArrayList<>();

	private Double latitud;

	private Double longitud;
	
	@PaisIso
	private String paisIso;
	
	@Size(max = 5000)
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

	public List<SectorDto> getSectores() {
		return sectores;
	}

	public void setSectores(List<SectorDto> sectores) {
		this.sectores = sectores;
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

	public String getPaisIso() {
		return paisIso;
	}

	public void setPaisIso(String paisIso) {
		this.paisIso = paisIso;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	@Override
	public String toString() {
		return "EscuelaDto [id=" + id + ", nombre=" + nombre + ", sectores=" + sectores + "]";
	}

}
