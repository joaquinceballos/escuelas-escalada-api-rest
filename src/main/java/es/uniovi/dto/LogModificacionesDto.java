package es.uniovi.dto;

import java.time.LocalDateTime;

import es.uniovi.domain.LogModificaciones.AccionLog;
import es.uniovi.domain.LogModificaciones.TipoRecurso;

public class LogModificacionesDto {

	private Long id;

	private String path;

	private UsuarioPublicoDto usuario;

	private LocalDateTime fecha;

	private AccionLog accionLog;

	private TipoRecurso tipoRecurso;

	private Long idRecurso;

	private String valorRecurso;

	public LogModificacionesDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public UsuarioPublicoDto getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioPublicoDto usuario) {
		this.usuario = usuario;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public AccionLog getAccionLog() {
		return accionLog;
	}

	public void setAccionLog(AccionLog accionLog) {
		this.accionLog = accionLog;
	}

	public TipoRecurso getTipoRecurso() {
		return tipoRecurso;
	}

	public void setTipoRecurso(TipoRecurso tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}

	public Long getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(Long idRecurso) {
		this.idRecurso = idRecurso;
	}

	public String getValorRecurso() {
		return valorRecurso;
	}

	public void setValorRecurso(String valorRecurso) {
		this.valorRecurso = valorRecurso;
	}

}
