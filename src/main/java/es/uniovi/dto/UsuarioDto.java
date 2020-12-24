package es.uniovi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UsuarioDto {

	private Long id;

	@NotBlank
	private String nombre;

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@JsonInclude(Include.NON_NULL)
	private String password;

	public UsuarioDto() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UsuarioDto [id=" + id + ", nombre=" + nombre + ", email=" + email + ", password=" + password + "]";
	}

}
