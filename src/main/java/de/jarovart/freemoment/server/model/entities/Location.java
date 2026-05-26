/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.model.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.Getter;
import lombok.Setter;

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
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "thumbnail_image_id")
    private Image thumbnailImage;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "image_order")
    private List<Image> images = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_user_id")
    private AppUser createdUser;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LocationLike> likes = new HashSet<>();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LocationJoin> joins = new HashSet<>();

    // Location thumbnail image display settings
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "scale", column = @Column(name = "thumbnail_scale")),
            @AttributeOverride(name = "offsetX", column = @Column(name = "thumbnail_offset_x")),
            @AttributeOverride(name = "offsetY", column = @Column(name = "thumbnail_offset_y"))
    })
    private ImageTransform locationImageTransform;

    //Need for DB
    public Location() {
    }

    public Location(String title, String description, String address, LocalDateTime creationDateTime,
                    LocalDateTime startDateTime, LocalDateTime endDateTime, Double latitude, Double longitude,
                    AppUser createdUser) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdUser = createdUser;
        this.likes = new HashSet<>();
        this.joins = new HashSet<>();
    }
}