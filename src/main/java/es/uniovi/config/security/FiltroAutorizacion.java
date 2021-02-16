package es.uniovi.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import es.uniovi.api.ApiUtils;
import es.uniovi.common.Constantes;

public class FiltroAutorizacion extends BasicAuthenticationFilter {
	
	private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler() {

		@Override
		public void handle(HttpServletRequest request, HttpServletResponse response,
				AccessDeniedException accessDeniedException) throws IOException, ServletException {
			Map<String, String> map = Collections.singletonMap("error", accessDeniedException.getLocalizedMessage());
			ApiUtils.errorResponse(response, map, HttpStatus.FORBIDDEN);
		}
		
	};
	
    private static final Logger _logger = LogManager.getLogger(FiltroAutorizacion.class);
	
	private String secret;
	
	public FiltroAutorizacion(AuthenticationManager authenticationManager, String secret) {
		super(authenticationManager);
		this.secret = secret;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(Constantes.HEADER_STRING);
		if (header == null || !header.startsWith(Constantes.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (JWTVerificationException e) {
			accessDeniedHandler.handle(req, res, new AccessDeniedException(e.getLocalizedMessage(), e));
		}		
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(Constantes.HEADER_STRING);
		if (token != null) {
			DecodedJWT decodedJWT = JWT
					.require(Algorithm.HMAC512(secret.getBytes()))
					.build()
					.verify(token.replace(Constantes.TOKEN_PREFIX, ""));
			String user = decodedJWT.getSubject();
			if (user != null) {
				List<GrantedAuthority> authorities = new ArrayList<>();
				// Saco los nombres de los roles del token
				Claim claimRoles = decodedJWT.getClaim("roles");
				try {
					List<String> roles = claimRoles.asList(String.class);
					for (String rol : roles) {
						authorities.add(new SimpleGrantedAuthority(rol));
					}
				} catch (JWTDecodeException e) {
					_logger.debug("Error convirtiendo lista de roles del claim:roles", e);
				}
				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}
			return null;
		}
		return null;
	}

}
