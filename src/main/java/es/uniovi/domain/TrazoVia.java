package es.uniovi.domain;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "via", "croquis" }, name = "UQ_TRAZO_VIA_CROQUIS_VIA"))
public class TrazoVia {
	
	@Id
	@GeneratedValue
	private Long id;

	@ElementCollection
	private List<Punto> puntos;
	
	@ManyToOne
	private Via via;
	
	@ManyToOne
	private Croquis croquis;
	
	public TrazoVia() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return "TrazoVia [id=" + id + ", via=" + via + ", croquis=" + croquis + "]";
	}
	
}
