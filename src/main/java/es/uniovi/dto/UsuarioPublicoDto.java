package es.uniovi.dto;

import javax.validation.constraints.NotBlank;

import es.uniovi.validation.PaisIso;

public class UsuarioPublicoDto {

	private Long id;

	@NotBlank
	private String nombre;

	@NotBlank
	private String apellido1;

	private String apellido2;

	@PaisIso
	private String pais;

	public UsuarioPublicoDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1.trim();
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		if (apellido2 != null) {
			this.apellido2 = apellido2.trim();
		}
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
}
