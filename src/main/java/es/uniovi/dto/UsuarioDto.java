package es.uniovi.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class UsuarioDto extends UsuarioPublicoDto {

	@NotBlank
	@JsonInclude(Include.NON_NULL)
	private String password;

	@NotBlank
	@Size(min = 4, max = 20)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	private String username;

	@Past
	private LocalDate nacimiento;

	@Email
	@NotBlank
	private String email;

	public UsuarioDto() {
		super();
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

	public LocalDate getNacimiento() {
		return nacimiento;
	}

	public void setNacimiento(LocalDate nacimiento) {
		this.nacimiento = nacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email.trim();
	}

	@Override
	public String toString() {
		return "UsuarioDto [password=" + password + ", username=" + username + ", nacimiento=" + nacimiento
				+ ", getId()=" + getId() + ", getNombre()=" + getNombre() + ", getEmail()=" + getEmail()
				+ ", getApellido1()=" + getApellido1() + ", getApellido2()=" + getApellido2() + ", getPais()="
				+ getPais() + "]";
	}

}
