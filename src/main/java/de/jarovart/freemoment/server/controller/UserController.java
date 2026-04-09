package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyProfileRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.dtos.response.MyUserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.model.enums.LocationType;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.UserLocationService;
import de.jarovart.freemoment.server.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // wichtig für Flutter
public class UserController {

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserLocationService userLocationService;

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("GET /api/users/all");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/query")
    public ResponseEntity<List<UserResponse>> byQuery(@RequestParam String query) {
        log.info("GET /api/users/query={}", query);
        return ResponseEntity.ok(userService.searchByQuery(query));
    }

    @GetMapping("/findById")
    public ResponseEntity<UserFullResponse> findById(@RequestParam long id) {
        log.info("GET /api/users/findById={}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/findByUsername")
    public ResponseEntity<UserFullResponse> findByUsername(@RequestParam String username) {
        log.info("GET /api/users/findByUsername={}", username);
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("/{id}/locations/{locationType}")
    public ResponseEntity<List<LocationResponse>> getLocationsCreatedByUserId(@PathVariable Long id,
                                                                              @PathVariable("locationType") String locationTypeString,
                                                                              Authentication authentication) {
        String username = (authentication != null) ? authentication.getName() : "";
        log.info("GET /api/users/{}/locations/{} by user {}", id, locationTypeString, username);

        LocationType locationType = LocationType.from(locationTypeString);
        return ResponseEntity.ok(
                userLocationService.getLocationsByUserId(id, locationType, username)
        );
    }


    @GetMapping("/me/locations/{locationType}")
    public ResponseEntity<List<LocationResponse>> getMyLocations(
            @PathVariable("locationType") String locationTypeString,
            Authentication authentication) {
        log.info("GET /api/users/me/locations/{} by me {}", locationTypeString, authentication.getName());

        LocationType locationType = LocationType.from(locationTypeString);
        return ResponseEntity.ok(userLocationService.getMyLocations(locationType, authentication.getName())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<MyUserFullResponse> getMyProfile(@AuthenticationPrincipal JarovartUserDetails userDetails) {
        log.info("GET /api/users/me={}", userDetails.getUsername());
        return ResponseEntity.ok(userService.getMyProfile(userDetails.getId()));
    }

    @PatchMapping("/me")
    public ResponseEntity<MyUserFullResponse> updateMyProfile(@Valid @RequestBody UpdateMyProfileRequest request,
                                                              @AuthenticationPrincipal JarovartUserDetails userDetails
    ) {
        log.info("Patch /api/users/me={}", userDetails.getUsername());
        return ResponseEntity.ok(userService.updateMyProfile(userDetails.getId(), request));
    }
}
