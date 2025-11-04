/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.data.Location;
import de.jarovart.freemoment.server.repository.LocationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Artem
 */
@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "http://localhost:3000") // wichtig für React
public class LocationController {
    
    @Autowired
    private LocationRepository repository;

    @PostMapping
    public Location createLocation(@RequestBody Location location) {
        System.out.println(location.toString());
        location.setId(null);
        return repository.save(location);
    }

    @GetMapping
    public List<Location> getAllLocations() {
        System.out.println("GET /api/locations wurde aufgerufen");
        return repository.findAll();
    }
    
    @GetMapping("/within")
    public List<Location> getLocationsWithinBounds(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng) {
        System.out.println("within /api/bounds wurde aufgerufen");
        List<Location> locations =repository.findByLatitudeBetweenAndLongitudeBetween(
                minLat, maxLat, minLng, maxLng);
        locations.stream().forEach(l -> System.out.println(l.toString()));
        return locations;
    }
}
