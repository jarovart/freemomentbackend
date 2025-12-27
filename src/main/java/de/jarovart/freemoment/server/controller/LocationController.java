/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.LocationBaseDTO;
import de.jarovart.freemoment.server.model.dtos.LocationCreateDTO;
import de.jarovart.freemoment.server.model.dtos.LocationFullDTO;
import de.jarovart.freemoment.server.services.LocationLikerService;
import de.jarovart.freemoment.server.services.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Artem
 */
@RestController
@RequestMapping("/api/locations")
//@CrossOrigin(origins = "http://localhost:3000") // wichtig für React
@CrossOrigin(origins = "*") // wichtig für Flutter
public class LocationController {

    private static final Logger log =
            LoggerFactory.getLogger(LocationController.class);
    @Autowired
    private LocationLikerService locationLikerService;
    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationBaseDTO>> getAllLocations(@RequestParam(defaultValue = "100") int limit) {
        log.info("GET /api/locations");
        return ResponseEntity.ok(locationService.getAllLocations(limit)
                                                .stream()
                                                .toList());
    }

    @GetMapping("/within")
    public ResponseEntity<List<LocationBaseDTO>> getLocationsWithinBounds(
            @RequestParam double minLat, @RequestParam double maxLat, @RequestParam double minLng,
            @RequestParam double maxLng) {
        log.info("GET /within bounds lat=[{},{}] lng=[{},{}]", minLat, maxLat, minLng, maxLng);

        return ResponseEntity.ok(locationService.getLocationsWithinBounds(minLat, maxLat, minLng, maxLng)
                                                .stream()
                                                .toList());
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationBaseDTO>> search(@RequestParam String query) {
        log.info("GET /search query={}", query);
        if (query == null || query.trim()
                                  .length() < 3) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        return ResponseEntity.ok(locationService.search(query.trim()));
    }

    @GetMapping("/findById")
    public ResponseEntity<LocationFullDTO> search(@RequestParam Long id) {
        log.info("GET /findById={} location wurde aufgerufen", id);
        return locationService.getLocationById(id)
                              .map(ResponseEntity::ok)           // 200 + Body
                              .orElse(ResponseEntity.notFound()
                                                    .build()); // 404
    }

    @GetMapping("/withinWithTime")
    public ResponseEntity<List<LocationBaseDTO>> getLocationsWithinBoundsWithTime(
            @RequestParam double minLat, @RequestParam double maxLat,
            @RequestParam double minLng, @RequestParam double maxLng,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd) {
        log.info("GET /within bounds lat=[{},{}] lng=[{},{}] between {} and {}", minLat, maxLat, minLng, maxLng,
                 rangeStart, rangeEnd);
        return ResponseEntity.ok(locationService.getLocationsWithinBoundsAndRange(minLat, maxLat, minLng, maxLng,
                                                                                  rangeStart, rangeEnd)
                                                .stream()
                                                .toList());
    }


    @PostMapping("/createLocation")
    @PreAuthorize("hasRole('USE_ROLE')")
    public ResponseEntity<LocationBaseDTO> createLocation(@Valid @RequestBody LocationCreateDTO locationCreateDTO
            , @AuthenticationPrincipal UserDetails principal) {
        log.info("POST /createLocation request: {} from {}", locationCreateDTO.getTitle(), principal.getUsername());
        LocationBaseDTO createdLocation = locationService.createLocation(locationCreateDTO, principal.getUsername());
        return ResponseEntity
                .status(HttpStatus.CREATED) // 🔥 201
                .body(createdLocation);
    }

    //

    /**
     * {@Code: Authentication authentication alternative mit mehr boilerplate}
     *
     * @param locationId
     * @param user
     * @return
     */
    @PostMapping("/{locationId}/like")
    public ResponseEntity<Void> like(@PathVariable Long locationId, @AuthenticationPrincipal UserDetails user) {
        locationLikerService.likeLocation(locationId, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{locationId}/like")
    public ResponseEntity<Void> unlike(@PathVariable Long locationId, @AuthenticationPrincipal UserDetails user) {
        locationLikerService.unlikeLocation(locationId, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinLocation(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {

        locationService.joinLocation(id, user.getUsername());
        return ResponseEntity.noContent().build(); // 204
    }

    @DeleteMapping("/{id}/join")
    public ResponseEntity<Void> leaveLocation(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {

        locationService.leaveLocation(id, user.getUsername());
        return ResponseEntity.noContent().build(); // 204
    }
}
