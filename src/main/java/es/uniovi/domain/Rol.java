package es.uniovi.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UQ_ROL_NOMBRE", columnNames = "NOMBRE"))
public class Rol implements Serializable {

	private static final long serialVersionUID = -8591357215885726940L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private NombreRol nombre;

	@ManyToMany(mappedBy = "roles")
	private List<Usuario> usuarios;

	@ManyToMany(fetch = FetchType.EAGER)
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

	public NombreRol getNombre() {
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

	public void setNombre(NombreRol nombre) {
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
