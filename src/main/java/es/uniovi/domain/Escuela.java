package es.uniovi.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.uniovi.validation.Coordenadas;
import es.uniovi.validation.PaisIso;

@Entity
@Coordenadas
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "NOMBRE" }, name = "UK_ESCUELA_NOMBRE") })
public class Escuela implements Ubicable, Serializable {

	private static final long serialVersionUID = 8023830574582405343L;
	
	private Double latitud;
	
	private Double longitud;
	
	@PaisIso
	private String paisIso;
	
	@Column(length = 5000)
	private String informacion;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "escuela", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<CierreTemporada> cierresTemporada = new HashSet<>();

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	@Column(nullable = false)
	private String nombre;

	@NotNull
	@OrderBy("id")
	@OneToMany(fetch = FetchType.EAGER,
	           orphanRemoval = true,
	           mappedBy = "escuela",
	           cascade = { CascadeType.ALL })
	private Set<@NotNull Sector> sectores = new HashSet<>();

	public Escuela() {
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

	public Set<Sector> getSectores() {
		return sectores;
	}

	public void setSectores(Set<Sector> sectores) {
		this.sectores = sectores;
	}	

	@Override
	public Double getLatitud() {
		return this.latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	@Override
	public Double getLongitud() {
		return this.longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public String getPaisIso() {
		return paisIso;
	}

	public void setPaisIso(String paisIso) {
		this.paisIso = paisIso;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	public Set<CierreTemporada> getCierresTemporada() {
		return cierresTemporada;
	}

	public void setCierresTemporada(Set<CierreTemporada> cierresTemporada) {
		this.cierresTemporada = cierresTemporada;
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
		Escuela other = (Escuela) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Escuela [id=" + id + ", nombre=" + nombre + ", sectores=" + sectores + "]";
	}

}
