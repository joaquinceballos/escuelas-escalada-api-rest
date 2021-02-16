package es.uniovi.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "sector", "nombre" }, name = "UQ_CROQUIS_NOMBRE"))
public class Croquis implements Serializable {

	private static final long serialVersionUID = 8145877771457034636L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "FK_CROQUIS_SECTOR"))
	private Sector sector;	
	
	@OneToMany(mappedBy = "croquis",
	           orphanRemoval = true,
	           cascade = { CascadeType.REMOVE })
	private List<TrazoVia> trazos;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] imagen;
	
	@Basic(optional = false)
	private String nombre;
	
	private String formatoImagen;

	public Croquis() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public byte[] getImagen() {
		return imagen;
	}

	public void setImagen(byte[] bs) {
		this.imagen = bs;
	}
	
	public List<TrazoVia> getTrazos() {
		return trazos;
	}

	public void setTrazos(List<TrazoVia> trazos) {
		this.trazos = trazos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getFormatoImagen() {
		return formatoImagen;
	}

	public void setFormatoImagen(String formatoImagen) {
		this.formatoImagen = formatoImagen;		
	}
	
	@Override
	public String toString() {
		return "Croquis [id=" + id + ", sector=" + sector + ", nombre=" + nombre + "]";
	}

}
