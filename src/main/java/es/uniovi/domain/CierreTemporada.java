package es.uniovi.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.uniovi.validation.PeriodoFechas;
import es.uniovi.validation.ValueOfEnum;

@Entity
@PeriodoFechas
public class CierreTemporada implements Serializable, Periodo {

	private static final long serialVersionUID = -1119871856531367284L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private LocalDate inicio;

	@Column(nullable = false)
	private LocalDate fin;

	@Column(nullable = false)
	@ValueOfEnum(enumClass = MotivoCierre.class)
	private String motivoCierre;
	
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_CIERRETEMPORADA_ESCUELA"))
	private Escuela escuela;

	public CierreTemporada() {
		super();
	}

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

	public Escuela getEscuela() {
		return escuela;
	}

	public void setEscuela(Escuela escuela) {
		this.escuela = escuela;
	}

	@Override
	public String toString() {
		return "CierreTemporada [id=" + id + ", inicio=" + inicio + ", fin=" + fin + ", motivoCierre=" + motivoCierre
				+ "]";
	}

}
