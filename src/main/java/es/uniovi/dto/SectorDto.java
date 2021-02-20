package es.uniovi.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
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

	@Valid
	@JsonInclude(Include.NON_NULL)
	private List<ViaDto> vias = new ArrayList<>();

	@Valid
	@JsonInclude(Include.NON_NULL)
	private HorasDeSolDto horasDeSol;

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

	public List<ViaDto> getVias() {
		return vias;
	}

	public void setVias(List<ViaDto> vias) {
		this.vias = vias;
	}

	public HorasDeSolDto getHorasDeSol() {
		return horasDeSol;
	}

	public void setHorasDeSol(HorasDeSolDto horasDeSol) {
		this.horasDeSol = horasDeSol;
	}

	@Override
	public String toString() {
		return "SectorDto [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", vias=" + vias + "]";
	}
}
