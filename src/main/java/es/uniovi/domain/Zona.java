package es.uniovi.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.Formula;

import es.uniovi.domain.LogModificaciones.TipoRecurso;
import es.uniovi.dto.ZonaDto;
import es.uniovi.validation.PaisIso;

@Entity(name = "zona")
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_ZONA_PAIS_REGION", columnNames = { "PAIS", "REGION" }))
public class Zona implements Serializable, RecursoLogeable {

	private static final long serialVersionUID = -8029538604557495453L;

	@Id
	@GeneratedValue
	private Long id;

	@PaisIso
	private String pais;

	@NotEmpty
	private String region;
	
	private String informacion;

	@Formula("(select count(*)" +
	         " from escuela e "+ 
			 " where e.zona = id)")
	private Integer numeroEscuelas;

	@Formula("(select count(*)" +
	         " from escuela e join sector s on (e.id = s.escuela) join via v on (s.id = v.sector) " +
			 " where e.zona = id)")
	private Integer numeroVias;
	
	@OrderBy("nombre")
	@OneToMany(mappedBy = "zona")
	private Set<Escuela> escuelas = new HashSet<>();

	public Zona() {
		super();
	}

	public Long getId() {
		return id;
	}

	public Integer getNumeroEscuelas() {
		return numeroEscuelas;
	}

	public Integer getNumeroVias() {
		return numeroVias;
	}

	public String getPais() {
		return pais;
	}

	public String getRegion() {
		return region;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNumeroEscuelas(Integer numeroEscuelas) {
		this.numeroEscuelas = numeroEscuelas;
	}

	public void setNumeroVias(Integer numeroVias) {
		this.numeroVias = numeroVias;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getInformacion() {
		return informacion;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	public Set<Escuela> getEscuelas() {
		return escuelas;
	}

	public void setEscuelas(Set<Escuela> escuelas) {
		this.escuelas = escuelas;
	}

	@Override
	public String toString() {
		return "Zona [id=" + id + ", pais=" + pais + ", region=" + region + ", numeroEscuelas=" + numeroEscuelas
				+ ", numeroVias=" + numeroVias + "]";
	}

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.ZONA;
	}

	@Override
	public String pathLog() {
		return "/zonas/" + this.id;
	}

	@Override
	public Class<?> claseSerializar() {
		return ZonaDto.class;
	}

}
