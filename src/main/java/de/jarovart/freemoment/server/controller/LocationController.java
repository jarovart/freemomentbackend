/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.requests.LocationCreateRequest;
import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyLocationRequest;
import de.jarovart.freemoment.server.model.dtos.requests.UpdateThumbnailRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.controllerservices.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private LocationService locationService;

    @GetMapping("/findById")
    public ResponseEntity<LocationFullResponse> findById(@RequestParam Long id,
                                                         @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /findById={} location wurde aufgerufen", id);
        Long userId = user != null ? user.getId() : null;
        LocationFullResponse loc = locationService.getLocationById(id, userId);
        return ResponseEntity.ok(loc);
    }

    @GetMapping("/{locationId}/like")
    public ResponseEntity<Boolean> hasLike(@PathVariable Long locationId,
                                           @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /api/locations/{}/like request by {}", locationId, user.getUsername());
        boolean returnedValue = locationService.hasUserLiked(locationId, user.getId());
        return ResponseEntity.ok(returnedValue);
    }

    @GetMapping("/{locationId}/join")
    public ResponseEntity<Boolean> hasJoin(@PathVariable Long locationId,
                                           @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /api/locations/{}/join request by {}", locationId, user.getUsername());
        boolean returnedValue = locationService.hasUserJoined(locationId, user.getId());
        return ResponseEntity.ok(returnedValue);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationResponse>> search(@RequestParam String query,
                                                         @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /search query={}", query);
        if (query == null || query.trim()
                                  .length() < 3) {
            return ResponseEntity.badRequest()
                                 .build();
        }
        Long userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(locationService.search(query.trim(), userId));
    }

    /**
     * Creates a Location Entity and returns a LocationResponse (Base), which will be used for location map.
     *
     * @param locationCreateRequest the request for creating a location.
     * @param user                  the current user, who created the location.
     * @return a location response.
     */
    @PostMapping("/createLocation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody LocationCreateRequest locationCreateRequest
            , @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("Authorities: {}", user.getAuthorities());
        log.info("POST /createLocation request: {} from {}", locationCreateRequest.getTitle(), user.getUsername());
        LocationResponse createdLocation = locationService.createLocation(locationCreateRequest,
                                                                          user.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED) // 🔥 201
                .body(createdLocation);
    }

    @PostMapping("/{locationId}/like")
    public ResponseEntity<Void> like(@PathVariable Long locationId, @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("POST /api/locations/{}/like request by {}", locationId, user.getUsername());
        locationService.likeLocation(locationId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{locationId}/join")
    public ResponseEntity<Void> joinLocation(@PathVariable Long locationId,
                                             @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("POST /api/locations/{}/join request by {}", locationId, user.getUsername());
        locationService.joinLocation(locationId, user.getId());
        return ResponseEntity.noContent().build(); // 204
    }

    @PatchMapping("/{locationId}/thumbnail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LocationResponse> updateThumbnailLocation(@PathVariable Long locationId,
                                                                    @RequestBody UpdateThumbnailRequest updateThumbnailRequest,
                                                                    @AuthenticationPrincipal JarovartUserDetails principal) {
        log.info("PATCH /updateThumbnail of locationid {} request: {} by {}", locationId,
                 updateThumbnailRequest.getImageId(), principal.getUsername());
        return ResponseEntity.ok(
                locationService.updateThumbnailLocation(locationId, updateThumbnailRequest, principal.getId()));
    }

    //@PatchMapping("/{locationId}")
    public ResponseEntity<LocationFullResponse> updateMyLocation(@PathVariable Long locationId,
                                                                 @Valid @RequestBody UpdateMyLocationRequest locationRequest,
                                                                 @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("PATCH /api/location1/{} updateMyLocation wurde aufgerufen", locationId);
        LocationFullResponse loc = locationService.updateMyLocation1(locationId, locationRequest, user.getId());
        return ResponseEntity.ok(loc);
    }

    @PatchMapping(
            value = "/{locationId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LocationFullResponse> updateMyLocation(
            @PathVariable Long locationId,
            @RequestPart("data")
            @Valid UpdateMyLocationRequest locationRequest,
            @RequestPart(value = "files", required = false)
            List<MultipartFile> files,
            @RequestParam(value = "clientKeys", required = false)
            String clientKeysCsv,
            @AuthenticationPrincipal JarovartUserDetails user
    ) {
        log.info("PATCH /api/location/{} updateMyLocation wurde aufgerufen", locationId);
        List<String> clientKeys = clientKeysCsv == null || clientKeysCsv.isBlank()
                ? List.of()
                : List.of(clientKeysCsv.split(","));

        LocationFullResponse loc = locationService.updateMyLocation(
                locationId,
                locationRequest,
                files == null ? Collections.emptyList() : files,
                clientKeys,
                user.getId()
        );

        return ResponseEntity.ok(loc);
    }

    @DeleteMapping("/{locationId}/like")
    public ResponseEntity<Void> unlike(@PathVariable Long locationId,
                                       @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("DELETE /api/locations/{}/like request by {}", locationId, user.getUsername());
        locationService.unlikeLocation(locationId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{locationId}/join")
    public ResponseEntity<Void> leaveLocation(@PathVariable Long locationId,
                                              @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("DELETE /api/locations/{}/join request by {}", locationId, user.getUsername());
        locationService.unjoinLocation(locationId, user.getId());
        return ResponseEntity.noContent().build(); // 204
    }

    /**********************************************************************************************
     *
     * Old data
     */


    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations(@RequestParam(defaultValue = "100") int limit,
                                                                  @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /api/locations");
        Long userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(locationService.getAllLocations(limit, userId));
    }

    @GetMapping("/within")
    public ResponseEntity<List<LocationResponse>> getLocationsWithinBounds(
            @RequestParam double minLat, @RequestParam double maxLat, @RequestParam double minLng,
            @RequestParam double maxLng,
            @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /within bounds lat=[{},{}] lng=[{},{}]", minLat, maxLat, minLng, maxLng);
        Long userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(locationService.getLocationsWithinBounds(minLat, maxLat, minLng, maxLng, userId));
    }

    @GetMapping("/withinWithTime")
    public ResponseEntity<List<LocationResponse>> getLocationsWithinBoundsWithTime(
            @RequestParam double minLat, @RequestParam double maxLat,
            @RequestParam double minLng, @RequestParam double maxLng,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
            @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /within bounds lat=[{},{}] lng=[{},{}] between {} and {}", minLat, maxLat, minLng, maxLng,
                 rangeStart, rangeEnd);
        Long userId = user != null ? user.getId() : null;
        return ResponseEntity.ok(locationService.getLocationsWithinBoundsAndRange(minLat, maxLat, minLng, maxLng,
                                                                                  rangeStart, rangeEnd, userId)
                                                .stream()
                                                .toList());
    }

    @GetMapping("/findByFilter")
    public Slice<LocationResponse> getSliceLocationsByFilterSettings(
            @RequestParam(required = false) String query,
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLng,
            @RequestParam double maxLng,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal JarovartUserDetails user
    ) {
        log.info(
                "GET /findByFilter query={} page={} size={} minLat={} maxLat={} minLng={} "
                        + "maxLng={} rangeStart={} rangeEnd={}",
                query, page, size, minLat, maxLat, minLng, maxLng, rangeStart, rangeEnd
        );

        Long userId = user != null ? user.getId() : null;

        var a = locationService.getSliceLocationsByFilterSettings(page, size, query, minLat, maxLat, minLng, maxLng,
                                                                  rangeStart, rangeEnd, userId);
        System.out.println(
                "filtersetting: " + a.stream().map(LocationResponse::getTitle).collect(Collectors.joining(", ")));
        return a;
    }
}
