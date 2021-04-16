package es.uniovi.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import es.uniovi.domain.LogModificaciones.TipoRecurso;

@Entity
@IdClass(TrazoViaPK.class)
public class TrazoVia implements Serializable, RecursoLogeable {

	private static final long serialVersionUID = 7095706231971332391L;
	
	@Id
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_TRAZO_VIA_VIA"))
	private Via via;
	
	@Id
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_TRAZO_VIA_CROQUIS"))
	private Croquis croquis;

	@ElementCollection
	@OrderColumn(name = "orden")
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_TRAZO_VIA_PUNTOS"))
	private List<Punto> puntos;
	
	public TrazoVia() {
		super();
	}

	public List<Punto> getPuntos() {
		return puntos;
	}

	public void setPuntos(List<Punto> puntos) {
		this.puntos = puntos;
	}

	public Via getVia() {
		return via;
	}

	public void setVia(Via via) {
		this.via = via;
	}

	public Croquis getCroquis() {
		return croquis;
	}

	public void setCroquis(Croquis croquis) {
		this.croquis = croquis;
	}

	@Override
	public String toString() {
		return "TrazoVia [via=" + via + ", croquis=" + croquis + "]";
	}

	@Override
	public Long getId() {
		// no es trazable!!
		return via.getId(); 
	}

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.TRAZO_VIA;
	}

	@Override
	public String pathLog() {
		return this.croquis.pathLog() + "via" + this.via.getId();
	}
	
}
