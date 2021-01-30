package es.uniovi.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_VIA_NOMBRE_SECTOR", columnNames = { "NOMBRE", "SECTOR" }))
public class Via {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
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
	
	@OneToMany(mappedBy = "via")
	private List<Ascension> ascenciones;

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

}
