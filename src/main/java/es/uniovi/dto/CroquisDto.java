package es.uniovi.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

public class CroquisDto {

	private Long id;

	private SectorDto sector;

	private List<TrazoViaDto> trazos;

	private String imagen;

	@NotBlank
	private String nombre;

	public CroquisDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SectorDto getSector() {
		return sector;
	}

	public void setSector(SectorDto sector) {
		this.sector = sector;
	}

	public List<TrazoViaDto> getTrazos() {
		return trazos;
	}

	public void setTrazos(List<TrazoViaDto> trazos) {
		this.trazos = trazos;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return "CroquisDto [id=" + id + ", sector=" + sector + ", nombre=" + nombre + "]";
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
