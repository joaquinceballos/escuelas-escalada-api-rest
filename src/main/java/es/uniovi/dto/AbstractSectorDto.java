package es.uniovi.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.uniovi.domain.Ubicable;
import es.uniovi.validation.Coordenadas;

@Coordenadas
public abstract class AbstractSectorDto implements Ubicable {

	private Long id;

	@NotBlank
	private String nombre;

	@JsonInclude(Include.NON_NULL)
	private Double latitud;

	@JsonInclude(Include.NON_NULL)
	private Double longitud;

	@Valid
	@JsonInclude(Include.NON_NULL)
	private HorasDeSolDto horasDeSol;
	
	@Size(max = 5000)
	@JsonInclude(Include.NON_NULL)
	private String informacion;

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

	public HorasDeSolDto getHorasDeSol() {
		return horasDeSol;
	}

	public void setHorasDeSol(HorasDeSolDto horasDeSol) {
		this.horasDeSol = horasDeSol;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	@Override
	public String toString() {
		return "SectorDto [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud + "]";
	}
}
