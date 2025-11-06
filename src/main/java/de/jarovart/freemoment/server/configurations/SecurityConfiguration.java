/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.auth.JwtAuthFilter;
import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.UserService;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Artem
 */
@Configuration
public class SecurityConfiguration {

  private final JwtService jwtService;
  private final UserService userService;

  public SecurityConfiguration(JwtService jwtService, UserService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
    JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtService, userService);

    http
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()
      .authorizeHttpRequests()
        .requestMatchers("/api/auth/**",
                "/api/locations",
                "/api/locations/within**",
                "/h2-console/**").permitAll()
        .anyRequest().authenticated()
    .and()
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // H2-Console erlaubt (dev)
    http.headers().frameOptions().disable();

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
