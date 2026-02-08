/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 *
 * @author Artem
 */
public interface UserRepository extends JpaRepository<AppUser, Long> {

    public Optional<AppUser> findByUsername(String username);

    public Optional<AppUser> findByEmail(String email);

    @Query("""
                SELECT u FROM AppUser u
                WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    Page<AppUser> searchUsers(@Param("query") String query, Pageable pageable);
}
