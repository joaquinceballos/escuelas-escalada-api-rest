package es.uniovi.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.uniovi.domain.LogModificaciones.TipoRecurso;
import es.uniovi.dto.ViaDto;
import es.uniovi.search.analyzer.NombreViaAnalyzer;

@Entity
@Indexed(index = "Via")
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_VIA_NOMBRE_SECTOR", columnNames = { "NOMBRE", "SECTOR" }))
public class Via implements Serializable, RecursoLogeable {

	private static final long serialVersionUID = -6118720572978189595L;

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	@Field(analyzer = @Analyzer(impl = NombreViaAnalyzer.class))
	private String nombre;

	@NotBlank
	private String grado; // TODO se debé validar el grado??

	@Min(0)
	private Integer numeroChapas;

	@Min(0)
	private Double longitud;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_VIA_SECTOR"))
	private Sector sector;
	
	@OneToMany(mappedBy = "via",
	           orphanRemoval = true)
	private List<Ascension> ascenciones;
	
	@OneToMany(mappedBy = "via",
	           orphanRemoval = true)
	private List<TrazoVia> trazos;

	public Via() {
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

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public Integer getNumeroChapas() {
		return numeroChapas;
	}

	public void setNumeroChapas(Integer numeroChapas) {
		this.numeroChapas = numeroChapas;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public List<Ascension> getAscenciones() {
		return ascenciones;
	}

	public void setAscenciones(List<Ascension> ascenciones) {
		this.ascenciones = ascenciones;
	}

	public List<TrazoVia> getTrazos() {
		return trazos;
	}

	public void setTrazos(List<TrazoVia> trazos) {
		this.trazos = trazos;
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
		Via other = (Via) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Via [id=" + id + ", nombre=" + nombre + ", grado=" + grado + ", numeroChapas=" + numeroChapas
				+ ", longitud=" + longitud + "]";
	}

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.VIA;
	}

	@Override
	public String pathLog() {
		return this.sector.pathLog() + "/vias/" + this.id;
	}

	@Override
	public Class<?> claseSerializar() {
		return ViaDto.class;
	}

}
