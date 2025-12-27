/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 * @author Artem
 */
public interface UserRepository extends JpaRepository<AppUser, Long> {

    public Optional<AppUser> findByUsername(String username);

    public Optional<AppUser> findByEmail(String email);
}
