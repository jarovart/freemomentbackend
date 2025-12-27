/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.model.entities;

import de.jarovart.freemoment.server.model.enums.UserRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Artem
 */
@Entity
@Getter
@Setter
@ToString(exclude = {
        "password",
        "createdLocations",
        "likedLocations",
        "joinedLocations"
})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String firstName;
    private String lastName;
    private String password; // BCrypt-hash
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "app_user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private Set<UserRole> roles;
    private String email;
    private LocalDateTime createdAt;

    // Banning fields
    private Boolean isBanned;
    private LocalDateTime bannedUntil;

    @OneToMany(mappedBy = "createdUser")
    private List<Location> createdLocations = new ArrayList<>();
    @ManyToMany(mappedBy = "likedByUsers")
    private Set<Location> likedLocations = new HashSet<>();
    @ManyToMany(mappedBy = "joinedUsers")
    private Set<Location> joinedLocations = new HashSet<>();

    public AppUser() {
    }
}
