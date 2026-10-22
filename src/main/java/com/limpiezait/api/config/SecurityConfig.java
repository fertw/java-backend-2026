package com.limpiezait.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		 http
         .csrf(csrf -> csrf.disable())
         .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/auth/**", "/api/ping", "/error").permitAll()
             .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")
             .requestMatchers("/api/**").hasRole("ADMIN")
             .anyRequest().authenticated())
         .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
     return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
