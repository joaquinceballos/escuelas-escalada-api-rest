package es.uniovi.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Past;

import es.uniovi.domain.LogModificaciones.TipoRecurso;
import es.uniovi.dto.UsuarioDto;
import es.uniovi.validation.PaisIso;

@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = "email", name = "uq_usuario_email"),
		@UniqueConstraint(columnNames = "username", name = "uq_usuario_username") })
public class Usuario implements Serializable, RecursoLogeable {

	private static final long serialVersionUID = 5481015999454416823L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nombre;
	
	private String password;
	
	private String email;
	
	private String username;
	
	private String apellido1;
	
	private String apellido2;
	
	@Past
	private LocalDate nacimiento;
	
	@PaisIso
	private String pais;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_rol",
	           joinColumns = @JoinColumn(name = "usuario_id",
	                                     referencedColumnName = "id", 
	                                     foreignKey = @ForeignKey(name = "fk_usurio_rol_usuario")),
	           inverseJoinColumns = @JoinColumn(name = "rol_id",
	                                            referencedColumnName = "id",
	                                            foreignKey = @ForeignKey(name = "fk_usurio_rol_rol")),
	           uniqueConstraints = @UniqueConstraint(name = "uq_usuario_rol", columnNames = { "usuario_id", "rol_id" }))
	private List<Rol> roles;
	
	@OneToMany(mappedBy = "usuario")
	private List<Ascension> ascenciones;
	
	@OneToMany(mappedBy = "usuario")
	private List<LogModificaciones> modificaciones;

	public Usuario() {
		super();
	}

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getPassword() {
		return password;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public List<Ascension> getAscenciones() {
		return ascenciones;
	}

	public void setAscenciones(List<Ascension> ascenciones) {
		this.ascenciones = ascenciones;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public LocalDate getNacimiento() {
		return nacimiento;
	}

	public void setNacimiento(LocalDate nacimiento) {
		this.nacimiento = nacimiento;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}	

	public List<LogModificaciones> getModificaciones() {
		return modificaciones;
	}

	public void setModificaciones(List<LogModificaciones> modificaciones) {
		this.modificaciones = modificaciones;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", password=" + password + ", email=" + email + ", roles="
				+ roles + "]";
	}

	@Override
	public TipoRecurso getTipo() {
		return TipoRecurso.USUARIO;
	}

	@Override
	public String pathLog() {
		return "/usuarios/" + this.id;
	}

	@Override
	public Class<?> claseSerializar() {
		return UsuarioDto.class;
	}

}
