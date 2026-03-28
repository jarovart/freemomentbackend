/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.controllerservices.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * @author Artem
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public JwtAuthFilter(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {

        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String username = jwtService.parseToken(token).getBody().getSubject();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    JarovartUserDetails userDetails = (JarovartUserDetails) authenticationService.loadUserByUsername(
                            username);
                    if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(userDetails, null,
                                                                        userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                // token invalid -> ignore, proceed without auth
            }
        }
        filterChain.doFilter(request, response);
    }
}

