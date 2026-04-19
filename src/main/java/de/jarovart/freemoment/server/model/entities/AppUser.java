/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.model.entities;

import de.jarovart.freemoment.server.model.enums.UserRole;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@ToString(exclude = {"password", "createdLocations", "likedLocations", "joinedLocations", "profileImage",
        "uploadedImages", "createdPlaces"})
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String firstName;
    private String lastName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Image profileImage;
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
    private String aboutMe;
    private LocalDateTime createdAt;

    // Banning fields
    private Boolean isBanned;
    private LocalDateTime bannedUntil;

    // User profile image display settings
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "scale", column = @Column(name = "profile_image_scale")),
            @AttributeOverride(name = "offsetX", column = @Column(name = "profile_image_offset_x")),
            @AttributeOverride(name = "offsetY", column = @Column(name = "profile_image_offset_y"))
    })
    private ImageTransform profileImageTransform;

    @OneToMany(mappedBy = "uploadedByUser", fetch = FetchType.LAZY)
    private List<Image> uploadedImages = new ArrayList<>();
    @OneToMany(mappedBy = "createdUser", fetch = FetchType.LAZY)
    private List<Location> createdLocations = new ArrayList<>();
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LocationLike> likedLocations = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LocationJoin> joinedLocations = new HashSet<>();
    @OneToMany(mappedBy = "creatorUser", fetch = FetchType.LAZY)
    private List<Place> createdPlaces = new ArrayList<>();

    public AppUser() {
    }
}
