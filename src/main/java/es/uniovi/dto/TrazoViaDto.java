package es.uniovi.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import es.uniovi.domain.Punto;

public class TrazoViaDto {

	@NotNull
	private ViaDto via;

	@NotEmpty
	@Size(min = 2)
	private List<Punto> puntos;

	public TrazoViaDto() {
		super();
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
