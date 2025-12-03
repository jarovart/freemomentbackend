/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.data;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Artem
 */
@Data
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime date;
    private Double latitude;
    private Double longitude;
    private String thumbnailUrl;
    private String imageUrl;
    private String locationUser;

    //Need for DB
    public Location() {
    }

    public Location(String title, String description, LocalDateTime date, Double latitude, Double longitude,
                    String thumbnailUrl, String imageUrl, String locationUser) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrl = imageUrl;
        this.locationUser = locationUser;
    }


    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
            System.out.println("Location.prePersist() - created new date: "+date.toString());
        }
    }
}