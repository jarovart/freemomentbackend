/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.auth.JwtAuthFilter;
import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

        return http.cors(Customizer.withDefaults())
                   .csrf(AbstractHttpConfigurer::disable)
                   .sessionManagement(session ->
                                              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(auth -> auth
                           .requestMatchers("/api/auth/**",
                                            "/api/locations",
                                            "/api/locations/findById**",
                                            "/api/locations/createLocation",
                                            "/api/locations/search**",
                                            "/api/locations/within**",
                                            "/api/locations/withinWithTime**",
                                            "/h2-console/**")
                           .permitAll()
                           .requestMatchers(HttpMethod.OPTIONS, "/api/locations/createasaLocation",
                                            "/api/auth/**")
                           .permitAll() // wichtig für React / CORS
                           .anyRequest()
                           .authenticated())
                   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                   .headers(headers ->
                                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                   .build();
    }

    // 🔧 CORS-Setup für React
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
