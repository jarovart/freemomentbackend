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

    private String name;
    private String description;
    private LocalDateTime date;
    private String imageUrl;
    private Double latitude;
    private Double longitude;

    //Need for DB
    public Location() {
    }

    public Location(String name, String description, LocalDateTime date, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
            System.out.println("Location.prePersist() - created new date: "+date.toString());
        }
    }
}