/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.auth;

import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 *
 * @author Artem
 */
public class JwtAuthFilter extends OncePerRequestFilter {
    
  private final JwtService jwtService;
  private final UserService userService;

  public JwtAuthFilter(JwtService jwtService, UserService userService) {
    this.jwtService = jwtService;
    this.userService = userService;
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
          UserDetails userDetails = userService.loadUserByUsername(username);
          if (jwtService.isTokenValid(token, userDetails.getUsername())) {
            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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

