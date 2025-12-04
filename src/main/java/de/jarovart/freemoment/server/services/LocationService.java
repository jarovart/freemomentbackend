package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.controller.LocationController;
import de.jarovart.freemoment.server.data.Location;
import de.jarovart.freemoment.server.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository repository;


    public Location createLocation(Location location) {
        location.setId(null);
        return repository.save(location);
    }

    public List<Location> getAllLocations() {
         return repository.findAll();
    }

    public List<Location> getLocationsWithinBounds(
             double minLat,  double maxLat, double minLng, double maxLng) {
        System.out.println("within /api/bounds wurde aufgerufen");
        return repository.findByLatitudeBetweenAndLongitudeBetween(
                minLat, maxLat, minLng, maxLng);
    }

    public List<Location> search(String query) {
        if (query == null || query.isBlank() || query.length() < 3) {
            return List.of();
        }
        return repository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
    }
}
