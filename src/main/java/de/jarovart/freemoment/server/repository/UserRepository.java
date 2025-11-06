/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.data.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Artem
 */
public interface UserRepository extends JpaRepository<AppUser, Long> {
    
  public Optional<AppUser> findByUsername(String username);
  public boolean existsByUsername(String username);
  public Optional<AppUser> findByEmail(String email);
  public Optional<AppUser> findByResetToken(String resetToken);
}
