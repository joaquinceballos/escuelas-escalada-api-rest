package es.uniovi.dto;

import java.time.LocalTime;

import es.uniovi.domain.PeriodoHoras;
import es.uniovi.validation.RangoHorario;

@RangoHorario
public class HorasDeSolDto implements PeriodoHoras {

	private LocalTime inicio;

	private LocalTime fin;

	public LocalTime getInicio() {
		return inicio;
	}

	public void setInicio(LocalTime inicio) {
		this.inicio = inicio;
	}

	public LocalTime getFin() {
		return fin;
	}

	public void setFin(LocalTime fin) {
		this.fin = fin;
	}

}
