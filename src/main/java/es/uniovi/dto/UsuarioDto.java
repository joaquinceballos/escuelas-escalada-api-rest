package es.uniovi.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import es.uniovi.validation.PaisIso;

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
	
	@NotBlank
	@Size(min = 4, max = 20)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	private String username;

	@NotBlank
	private String apellido1;

	private String apellido2;
	
	@Past
	private LocalDate nacimiento;
	
	@PaisIso
	private String pais;
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
		this.email = email.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.trim();
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

	@Override
	public String toString() {
		return "UsuarioDto [id=" + id + ", nombre=" + nombre + ", email=" + email + ", password=" + password + "]";
	}

}
