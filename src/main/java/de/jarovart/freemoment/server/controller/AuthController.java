/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.data.AppUser;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 *
 * @author Artem
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
    String username = body.get("username");
    String password = body.get("password");
    if (userRepository.existsByUsername(username)) return ResponseEntity.badRequest().body("Username exists");
    AppUser user = new AppUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRoles(Set.of("ROLE_USER"));
    userRepository.save(user);
    return ResponseEntity.ok(Map.of("username", user.getUsername()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
    String username = body.get("username");
    String password = body.get("password");
    Optional<AppUser> o = userRepository.findByUsername(username);
    if (o.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
    AppUser u = o.get();
    if (!passwordEncoder.matches(password, u.getPassword())) return ResponseEntity.status(401).body("Invalid credentials");

    String token = jwtService.generateToken(u.getUsername(), Map.of("roles", String.join(",", u.getRoles())));
    return ResponseEntity.ok(Map.of("token", token, "username", u.getUsername()));
  }
}

