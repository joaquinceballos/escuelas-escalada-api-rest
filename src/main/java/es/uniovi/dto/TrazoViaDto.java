package es.uniovi.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import es.uniovi.domain.Punto;

public class TrazoViaDto {

	private Long id;

	@NotNull
	private ViaDto via;

	@NotEmpty
	private List<Punto> puntos;

	public TrazoViaDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ViaDto getVia() {
		return via;
	}

	public void setVia(ViaDto via) {
		this.via = via;
	}

	public List<Punto> getPuntos() {
		return puntos;
	}

	public void setPuntos(List<Punto> puntos) {
		this.puntos = puntos;
	}

}
