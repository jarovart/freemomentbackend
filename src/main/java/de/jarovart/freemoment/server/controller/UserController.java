package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyProfileRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.dtos.response.MyUserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.controllerservices.LocationService;
import de.jarovart.freemoment.server.services.controllerservices.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private LocationService locationService;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Slice<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/users/all");
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/query")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UserResponse>> byQuery(@RequestParam String query) {
        log.info("GET /api/users/query={}", query);
        return ResponseEntity.ok(userService.searchByQuery(query));
    }

    @GetMapping("/findById")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserFullResponse> findById(@RequestParam long id) {
        log.info("GET /api/users/findById={}", id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/findByUsername")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserFullResponse> findByUsername(@RequestParam String username) {
        log.info("GET /api/users/findByUsername={}", username);
        return ResponseEntity.ok(userService.findByUsername(username));
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

    @GetMapping("{userId}/locations/created")
    public ResponseEntity<Slice<LocationResponse>> getCreatedLocationsByUserIdPaged(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @AuthenticationPrincipal JarovartUserDetails userDetails
    ) {
        log.info("GET getCreatedLocationsByUserIdPaged /api/users/{}/locations/created page={} size={}", userId, page,
                 pageSize);
        return ResponseEntity.ok(locationService.getCreatedLocationsByUserIdPaged(userId, page, pageSize)
        );
    }

    @GetMapping("{userId}/locations/liked")
    public ResponseEntity<Slice<LocationResponse>> getLikedLocationsByUserIdPaged(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @AuthenticationPrincipal JarovartUserDetails userDetails
    ) {
        log.info("GET getLikedLocationsByUserIdPaged /api/users/{}/location/liked page={} size={}", userId, page,
                 pageSize);
        return ResponseEntity.ok(
                userService.getLikedLocationsByUserId(userId, page, pageSize)
        );
    }

    @GetMapping("{userId}/locations/joined")
    public ResponseEntity<Slice<LocationResponse>> getJoinedLocationsByUserIdPaged(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal JarovartUserDetails userDetails
    ) {
        log.info("GET getJoinedLocationsByUserIdPaged /api/users/{}/locations/joined page={} size={}", userId, page,
                 size);
        return ResponseEntity.ok(userService.getJoinedLocationsByUserId(userId, page, size)
        );
    }
}
