package es.uniovi.domain;

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

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "uq_usuario_nombre"))
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nombre;
	
	private String password;
	
	private String email;

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

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", password=" + password + ", email=" + email + ", roles="
				+ roles + "]";
	}

}
