/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.data.Location;
import de.jarovart.freemoment.server.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;

import de.jarovart.freemoment.server.services.LocationService;
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
//@CrossOrigin(origins = "http://localhost:3000") // wichtig für React
@CrossOrigin(origins = "*") // wichtig für Flutter
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping("/createLocation")
    public Location createLocation(@RequestBody Location location) {
        System.out.println(location.toString());
        return locationService.createLocation(location);
    }

    @GetMapping
    public List<LocationBaseDTO> getAllLocations() {
        System.out.println("GET /api/locations wurde aufgerufen");
        return transformToDTOs(locationService.getAllLocations());
    }
    
    @GetMapping("/within")
    public List<Location> getLocationsWithinBounds(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng) {
        System.out.println("within /api/bounds wurde aufgerufen");
        return locationService.getLocationsWithinBounds(minLat, maxLat, minLng, maxLng);
    }

    @GetMapping("/search")
    public List<LocationBaseDTO> search(@RequestParam String query) {
        System.out.println("GET /search wurde aufgerufen");
        return transformToDTOs(locationService.search(query));
    }

    private List<LocationBaseDTO> transformToDTOs(List<Location> locations) {
        return locations.stream().map(loc -> new LocationBaseDTO(
                loc.getId(),
                loc.getTitle(),
                loc.getDate(),
                loc.getLatitude(),
                loc.getLongitude(),
                loc.getThumbnailUrl()
        )).toList();
    }

    public record LocationBaseDTO(
            Long id,
            String title,
            LocalDateTime date,
            Double latitude,
            Double longitude,
            String thumbnailUrl
    ) {}
}
