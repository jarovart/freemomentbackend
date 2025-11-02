/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.data;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
    private Double longtitude;

    //Need for DB
    public Location() {
    }
    
    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
            System.out.println("Location.prePersist() - created new date: "+date.toString());
        }
    }
}