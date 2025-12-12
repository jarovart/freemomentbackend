/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.auth.JwtAuthFilter;
import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.UserService;
import java.util.List;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                "/api/locations/createLocation",
                "/api/locations/search**",
                "/api/locations/within**",
                "/h2-console/**").permitAll()
        .requestMatchers(HttpMethod.OPTIONS, "/api/locations/createasaLocation")
            .permitAll() // 💡 <- wichtig für React
        .anyRequest().authenticated()
    .and()
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    // H2-Console erlaubt (dev)
    http.headers().frameOptions().disable();

    return http.build();
  }
  
  // 🔧 CORS-Setup für React
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // React URL
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
