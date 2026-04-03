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


    @Query("select count(ul) from AppUser u join u.createdLocations ul where u.id = :id")
    long countCreatedLocations(@Param("id") long id);

    @Query("select count(ul) from AppUser u join u.likedLocations ul where u.id = :id")
    long countLikedLocations(@Param("id") long id);

    @Query("select count(ul) from AppUser u join u.joinedLocations ul where u.id = :id")
    long countJoinedLocations(@Param("id") long id);

    @Query("""
                select u from AppUser u
                left join fetch u.profileImage
                where u.id = :id
            """)
    Optional<AppUser> findByIdFull(@Param("id") Long id);
}
