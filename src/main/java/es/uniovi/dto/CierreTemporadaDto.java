package es.uniovi.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import es.uniovi.domain.MotivoCierre;
import es.uniovi.domain.PeriodoDias;
import es.uniovi.validation.PeriodoFechas;
import es.uniovi.validation.ValueOfEnum;

@PeriodoFechas
public class CierreTemporadaDto implements PeriodoDias {

	private Long id;

	@NotNull
	private LocalDate inicio;

	@NotNull
	private LocalDate fin;

	@NotNull
	@ValueOfEnum(enumClass = MotivoCierre.class)
	private String motivoCierre;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getInicio() {
		return inicio;
	}

	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}

	public LocalDate getFin() {
		return fin;
	}

	public void setFin(LocalDate fin) {
		this.fin = fin;
	}

	public String getMotivoCierre() {
		return motivoCierre;
	}

	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	@Override
	public String toString() {
		return "CierreTemporadaDto [id=" + id + ", inicio=" + inicio + ", fin=" + fin + ", motivoCierre=" + motivoCierre
				+ "]";
	}

}
