package es.uniovi.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "IND_LOG_MODIFICACIONES_FECHA", columnList = "fecha"))
public class LogModificaciones implements Serializable {

	private static final long serialVersionUID = -4342654337217740102L;

	public enum AccionLog {
		CREAR, BORRAR, ACTUALIZAR
	}

	public enum TipoRecurso {
		ZONA, ESCUELA, SECTOR, VIA, CROQUIS, ASCENSION, COMENTARIO, USUARIO, CIERRE_TEMPORADA, HORAS_DE_SOL, TRAZO_VIA
	}

	@Id
	@GeneratedValue
	private Long id;

	private String path;

	@ManyToOne
	private Usuario usuario;

	private LocalDateTime fecha;

	@Enumerated(EnumType.STRING)
	private AccionLog accionLog;

	@Enumerated(EnumType.STRING)
	private TipoRecurso tipoRecurso;
	
	private Long idRecurso;
	
	@Lob
	private String valorRecurso;

	public LogModificaciones() {
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
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

	@Override
	public String toString() {
		return "LogModificaciones [id=" + id + ", path=" + path + ", usuario=" + usuario + ", fecha=" + fecha
				+ ", accionLog=" + accionLog + ", tipoRecurso=" + tipoRecurso + ", idRecurso=" + idRecurso + "]";
	}	

}
