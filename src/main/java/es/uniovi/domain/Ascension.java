package es.uniovi.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.uniovi.domain.LogModificaciones.TipoRecurso;

@Entity
public class Ascension implements Serializable, RecursoLogeable {

	private static final long serialVersionUID = 2165547851234215737L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ASCENSION_USUARIO"))
	private Usuario usuario;

	@ManyToOne(optional = false)
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_ASCENSION_VIA"))
	private Via via;

	private LocalDate fecha;

	private String comentario;

	private String grado;
	
	public Ascension() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Via getVia() {
		return via;
	}

	public void setVia(Via via) {
		this.via = via;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	@Override
	public String toString() {
		return "Ascension [usuario=" + usuario + ", via=" + via + ", fecha=" + fecha + ", comentario=" + comentario
				+ ", grado=" + grado + "]";
	}

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.ASCENSION;
	}

	@Override
	public String pathLog() {
		return this.usuario.pathLog() + "/ascensiones/" + this.id;
	}

}
