package es.uniovi.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class Rol {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nombre;

	@ManyToMany(mappedBy = "roles")
	private List<Usuario> usuarios;

	@ManyToMany
	@JoinTable(name = "rol_privilegio",
	           joinColumns = @JoinColumn(name = "rol_id",
	                                     referencedColumnName = "id",
	                                     foreignKey = @ForeignKey(name = "fk_rol_privilegio_rol")),
               inverseJoinColumns = @JoinColumn(name = "privilegio_id",
                                                referencedColumnName = "id",
                                                foreignKey = @ForeignKey(name = "fk_rol_privilegio_privilegio")),
               uniqueConstraints = @UniqueConstraint(name = "uq_rol_privilegio",
                                                     columnNames = { "rol_id", "privilegio_id" }))
	private List<Privilegio> privilegios;

	public Rol() {
		super();
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public List<Privilegio> getPrivilegios() {
		return privilegios;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrivilegios(List<Privilegio> privilegios) {
		this.privilegios = privilegios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	@Override
	public String toString() {
		return "Rol [id=" + id + ", nombre=" + nombre + ", usuarios=" + usuarios + ", privilegios=" + privilegios + "]";
	}

}
