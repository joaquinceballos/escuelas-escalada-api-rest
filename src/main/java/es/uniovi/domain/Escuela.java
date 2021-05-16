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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import es.uniovi.domain.LogModificaciones.TipoRecurso;
import es.uniovi.dto.EscuelaDto;
import es.uniovi.search.analyzer.ApiAnalyzer;
import es.uniovi.search.analyzer.NombreEscuelaAnalyzer;
import es.uniovi.validation.Coordenadas;

@Coordenadas
@Entity(name = "escuela")
@Indexed(index = "Escuela")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "NOMBRE" }, name = "UK_ESCUELA_NOMBRE") })
public class Escuela implements Ubicable, Serializable, RecursoLogeable {

	private static final long serialVersionUID = 8023830574582405343L;
	
	private Double latitud;
	
	private Double longitud;
		
	@Column(length = 5000)
	@Field(analyzer = @Analyzer(impl = ApiAnalyzer.class))
	private String informacion;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "escuela", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<CierreTemporada> cierresTemporada = new HashSet<>();

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	@Column(nullable = false)
	@Field(analyzer = @Analyzer(impl = NombreEscuelaAnalyzer.class))
	private String nombre;

	@NotNull
	@OrderBy("id")
	@OneToMany(fetch = FetchType.EAGER,
	           orphanRemoval = true,
	           mappedBy = "escuela",
	           cascade = { CascadeType.ALL })
	private Set<@NotNull Sector> sectores = new HashSet<>();
	
	@ManyToOne(optional = true)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ESCUELA_ZONA"))
	private Zona zona;

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

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
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

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.ESCUELA;
	}

	@Override
	public String pathLog() {
		return "/escuelas/" + this.id;
	}

	@Override
	public Class<?> claseSerializar() {
		return EscuelaDto.class;
	}

}
