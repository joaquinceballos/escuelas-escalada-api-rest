package es.uniovi.domain;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import es.uniovi.domain.LogModificaciones.TipoRecurso;
import es.uniovi.dto.HorasDeSolDto;
import es.uniovi.validation.RangoHorario;

@Entity
@RangoHorario
public class HorasDeSol implements Serializable, PeriodoHoras, RecursoLogeable {

	private static final long serialVersionUID = -6138325795162587160L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private LocalTime inicio;

	private LocalTime fin;

	@OneToOne
	@JoinColumn(name = "sector_id")
	private Sector sector;

	public HorasDeSol() {
		super();
	}

	@Override
	public String toString() {
		return "HorasDeSol [inicio=" + inicio + ", fin=" + fin + "]";
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.HORAS_DE_SOL;
	}

	@Override
	public String pathLog() {
		return this.sector.pathLog() + "/horassol/" + this.id;
	}

	@Override
	public Class<?> claseSerializar() {
		return HorasDeSolDto.class;
	}

}
