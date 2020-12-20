package es.uniovi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
			.antMatchers(HttpMethod.PUT, "/usuarios/**").hasRole(Constantes.ROLE_ADMIN)
			.anyRequest().authenticated()
			.and()
			.addFilter(new FiltroAutenticacion(authenticationManager(), secret))
			.addFilter(new FiltroAutorizacion(authenticationManager(), secret));
	}

}
