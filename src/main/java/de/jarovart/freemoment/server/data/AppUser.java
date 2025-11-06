/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Artem
 */
@Data
@Entity
public class AppUser {
    
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String username;
  private String password; // BCrypt-hash
  private Set<String> roles;
  private String email;
  private Boolean isActive;
  private String resetToken;
  private LocalDateTime resetTokenExpiry;
  
  public AppUser(){      
  }
}
