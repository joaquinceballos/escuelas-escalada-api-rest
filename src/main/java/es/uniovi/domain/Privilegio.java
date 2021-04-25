package es.uniovi.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_PRIVILEGIO_NOMBRE", columnNames = "NOMBRE"))
public class Privilegio implements Serializable {

	private static final long serialVersionUID = 568876450650919715L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private NombrePrivilegio nombre;

	@ManyToMany(mappedBy = "privilegios", fetch = FetchType.EAGER)
	private List<Rol> roles;

	public Privilegio() {
		super();
	}

	public Long getId() {
		return id;
	}

	public NombrePrivilegio getNombre() {
		return nombre;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(NombrePrivilegio nombre) {
		this.nombre = nombre;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Privilegio [id=" + id + ", nombre=" + nombre + ", roles=" + roles + "]";
	}

}
