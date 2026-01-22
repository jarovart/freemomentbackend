/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Artem
 */
@Entity
@Getter
@Setter
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String address;
    @Column(nullable = false)
    private LocalDateTime creationDateTime;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    private String thumbnailUrl;
    private List<String> imageUrls;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_user_id")
    private AppUser createdUser;
    @ManyToMany
    @JoinTable(name = "location_liked_by_users",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> likedByUsers = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "location_joined_users",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> joinedUsers = new HashSet<>();

    //Need for DB
    public Location() {
    }

    public Location(String title, String description, String address, LocalDateTime creationDateTime,
                    LocalDateTime startDateTime, LocalDateTime endDateTime, Double latitude, Double longitude,
                    List<String> imageUrls, AppUser createdUser) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.thumbnailUrl = thumbnailUrl;
        this.imageUrls = imageUrls;
        this.createdUser = createdUser;
        this.likedByUsers = new HashSet<>();
        this.joinedUsers = new HashSet<>();
    }


    //@PrePersist
    public void prePersist() {
        if (creationDateTime == null) {
            creationDateTime = LocalDateTime.now();
            System.out.println("Location.prePersist() - created new date: " + creationDateTime.toString());
        }
        if (createdUser == null) {
            createdUser = new AppUser();
            Faker faker = new Faker();
            createdUser.setUsername(faker.internet().username());
            createdUser.setEmail(faker.internet().emailAddress());
            System.out.println(
                    "Location.prePersist() - created new user: " + createdUser.getUsername() + ", " + createdUser.getEmail());
        }
        if (joinedUsers.isEmpty()) {
            Faker faker = new Faker();
            IntStream.range(0, (int) (Math.random() * 10)).forEach(
                    i -> {
                        AppUser user = new AppUser();
                        user.setUsername(faker.internet().username());
                        user.setEmail(faker.internet().emailAddress());
                        joinedUsers.add(user);
                        likedByUsers.add(user);
                    }
            );
            System.out.println("Location.prePersist() - created " + joinedUsers.size() + " joined/likedBy users: " +
                                       joinedUsers.stream()
                                                  .map(AppUser::getUsername)
                                                  .collect(Collectors.joining(",")));
        }
    }
}