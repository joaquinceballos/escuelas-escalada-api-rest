package es.uniovi.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.uniovi.search.analyzer.NombreSectorAnalyzer;
import es.uniovi.validation.Coordenadas;

@Entity
@Coordenadas
@Indexed(index = "Sector")
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_SECTOR_NOMBRE_ESCUELA", columnNames = { "NOMBRE", "ESCUELA" }))
public class Sector implements Ubicable, Serializable {

	private static final long serialVersionUID = 6121972409424227333L;

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	@Field(analyzer = @Analyzer(impl = NombreSectorAnalyzer.class))
	private String nombre;

	private Double latitud;

	private Double longitud;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_SECTOR_ESCUELA"))
	private Escuela escuela;

	@NotNull
	@OrderBy("id")
	@OneToMany(mappedBy = "sector",
	           orphanRemoval = true,
	           fetch = FetchType.EAGER,
	           cascade = { CascadeType.REMOVE })
	private Set<@NotNull Via> vias = new HashSet<>();

	@NotNull
	@OrderBy("id")
	@OneToMany(mappedBy = "sector",
	           orphanRemoval = true,
	           fetch = FetchType.EAGER,
	           cascade = { CascadeType.REMOVE })
	private Set<@NotNull Croquis> croquis = new HashSet<>();
	
	@JsonInclude(content = Include.NON_NULL)
	@OneToOne(mappedBy = "sector", optional = true, cascade = CascadeType.REMOVE)
	private HorasDeSol horasDeSol;

	@Column(length = 5000)
	private String informacion;

	public Sector() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Escuela getEscuela() {
		return escuela;
	}

	public void setEscuela(Escuela escuela) {
		this.escuela = escuela;
	}

	public Set<Via> getVias() {
		return vias;
	}

	public void setVias(Set<Via> vias) {
		this.vias = vias;
	}

	public Set<Croquis> getCroquis() {
		return croquis;
	}

	public void setCroquis(Set<Croquis> croquis) {
		this.croquis = croquis;
	}

	public HorasDeSol getHorasDeSol() {
		return horasDeSol;
	}

	public void setHorasDeSol(HorasDeSol horasDeSol) {
		this.horasDeSol = horasDeSol;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sector other = (Sector) obj;
		if (id == null) {
			return false;	
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sector [id=" + id + ", nombre=" + nombre + ", latitud=" + latitud + ", longitud=" + longitud
				+ ", vias=" + vias + "]";
	}

}
