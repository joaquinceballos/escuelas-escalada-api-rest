package es.uniovi.config.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.uniovi.api.ApiResponse;
import es.uniovi.api.ApiResponseStatus;
import es.uniovi.common.Constantes;
import es.uniovi.dto.UsuarioDto;

public class FiltroAutenticacion extends UsernamePasswordAuthenticationFilter {
	
	private String secret;
	
	public FiltroAutenticacion(AuthenticationManager authenticationManager, String secret) {
		super(authenticationManager);
		this.secret = secret;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			UsuarioDto credenciales = new ObjectMapper().readValue(request.getInputStream(), UsuarioDto.class);
			if (credenciales.getEmail() == null || credenciales.getPassword() == null) {
				throw new BadCredentialsException("email o contraseña no válidos: " + credenciales);
			} else {
				Authentication authenticacion = new UsernamePasswordAuthenticationToken(
						credenciales.getEmail(),
						credenciales.getPassword());
				return getAuthenticationManager().authenticate(authenticacion);
			}
		} catch (IOException e) {
			throw new InternalAuthenticationServiceException(e.getLocalizedMessage());
		}
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {        
		ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(
				Collections.singletonMap("token", getToken(authResult)),
				ApiResponseStatus.SUCCESS);
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(new ObjectMapper().writeValueAsString(apiResponse));
	}

	private String getToken(Authentication authResult) {
		
		// fecha de expiración
		Instant exp = LocalDateTime
				.now()
				.plus(Constantes.EXPIRATION_TIME, ChronoUnit.MILLIS)
				.toInstant(OffsetDateTime.now(ZoneId.systemDefault()).getOffset());

		// saco los roles del User y los añado al token
		List<String> roles = ((User) authResult.getPrincipal())
				.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		// retorno nuevo token firmado
		return JWT
				.create()
        		.withClaim("roles", roles)
                .withSubject(((User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(Date.from(exp))
                .withIssuedAt(new Date())
                .sign(HMAC512(secret.getBytes()));
	}
}
