package es.uniovi.config;



import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import es.uniovi.api.ApiUtils;
import es.uniovi.common.Constantes;
import es.uniovi.config.security.FiltroAutenticacion;
import es.uniovi.config.security.FiltroAutorizacion;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
		
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(4);
	}
	
	@Value("${spring.security.jwt.secret}")
	private String secret;
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.cors()
			.and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/login").permitAll()
			.antMatchers(HttpMethod.POST, "/usuarios").permitAll()
			.antMatchers(HttpMethod.GET, "/usuarios").hasRole(Constantes.ROLE_ADMIN)
			.antMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole(Constantes.ROLE_ADMIN)
			.antMatchers(HttpMethod.PUT, "/usuarios/*/ascensiones/*").hasAnyRole(Constantes.ROLE_USER, Constantes.ROLE_ADMIN)
			.antMatchers(HttpMethod.PUT, "/usuarios/**").hasRole(Constantes.ROLE_ADMIN)
			.anyRequest().authenticated()			
			.and()
			.exceptionHandling()
			.accessDeniedHandler(new AccessDeniedHandler() {

				@Override
				public void handle(HttpServletRequest request,
				                   HttpServletResponse response,
				                   AccessDeniedException accessDeniedException) throws IOException, ServletException {
					Map<String, String> map = new HashMap<>(4);
					map.put("path", request.getRequestURI());
					map.put("error", accessDeniedException.getLocalizedMessage());
					ApiUtils.errorResponse(response, map, HttpStatus.FORBIDDEN);
				}

			})
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new AuthenticationEntryPoint() {

				@Override
				public void commence(HttpServletRequest request,
				                     HttpServletResponse response,
				                     AuthenticationException authException) throws IOException, ServletException {
					Map<String, String> map = Collections.singletonMap("error", authException.getLocalizedMessage());
					ApiUtils.errorResponse(response, map, HttpStatus.UNAUTHORIZED);
				}

			})
			.and()
			.addFilter(new FiltroAutenticacion(authenticationManager(), secret))
			.addFilter(new FiltroAutorizacion(authenticationManager(), secret));
	}
	
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
