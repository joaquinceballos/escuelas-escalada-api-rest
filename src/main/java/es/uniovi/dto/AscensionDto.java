package es.uniovi.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AscensionDto {

	private Long id;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate fecha;

	private String comentario;

	private String grado;

	private ViaDto via;

	private UsuarioPublicoDto usuario;

	public AscensionDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ViaDto getVia() {
		return via;
	}

	public void setVia(ViaDto via) {
		this.via = via;
	}

	public UsuarioPublicoDto getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPublicoDto usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "AscensionDto [id=" + id + ", fecha=" + fecha + ", comentario=" + comentario + ", grado=" + grado + "]";
	}
}
