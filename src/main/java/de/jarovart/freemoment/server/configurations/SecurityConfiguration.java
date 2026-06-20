/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.controllerservices.AuthenticationService;
import de.jarovart.freemoment.server.util.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    public SecurityConfiguration(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtService, authenticationService);

        return http.cors(Customizer.withDefaults())
                   .csrf(AbstractHttpConfigurer::disable)
                   .sessionManagement(session ->
                                              session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                   .authorizeHttpRequests(auth -> auth
                           .requestMatchers("/api/auth/**",
                                            "/h2-console/**"
                           )
                           .permitAll()
                           .requestMatchers(HttpMethod.GET,
                                            "/api/images/**",
                                            "/api/info/status",
                                            "/api/locations/**",
                                            "/api/locations/findByFilter",
                                            "/api/locations/findById**",
                                            "/api/locations/search**",
                                            "/api/locations/within**",
                                            "/api/locations/withinWithTime**",
                                            "/api/places*",
                                            "/api/users/findByUsername*",
                                            "/api/users/*/locations/created",
                                            "/api/users/*/locations/joined"
                           ).permitAll()
                           .requestMatchers(HttpMethod.OPTIONS,
                                            "/api/auth/**",
                                            "/api/users/me").permitAll()
                           .requestMatchers(HttpMethod.GET,
                                            "/api/info/fullStatus",
                                            "/api/users/all",
                                            "/api/users/*/locations/created",
                                            "/api/users/*/locations/joined",
                                            "/api/users/query",
                                            "/api/users/id",
                                            "/api/users/me",
                                            "/api/users/*/locations/liked",
                                            "/api/locations/*/like",
                                            "/api/locations/*/join",
                                            "/api/settings/me").authenticated()
                           .requestMatchers(HttpMethod.DELETE,
                                            "/api/locations/*/like",
                                            "/api/locations/*/join").authenticated()
                           .requestMatchers(HttpMethod.POST,
                                            "/api/images/upload",
                                            "/api/locations/createLocation",
                                            "/api/locations/*/like",
                                            "/api/locations/*/join").authenticated()
                           .requestMatchers(HttpMethod.PUT,
                                            "/api/users/me").authenticated()
                           .requestMatchers(HttpMethod.PATCH,
                                            "/api/users/me",
                                            "/api/locations/*").authenticated()
                           .requestMatchers("/api/auth/**", "/h2-console/**", "/error").permitAll()
                           .anyRequest()
                           .authenticated())
                   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                   .headers(headers ->
                                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                   .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
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
