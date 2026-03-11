package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyProfileRequest;
import de.jarovart.freemoment.server.model.dtos.response.MyUserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
        log.info("GET /api/users/id={}", id);
        return userService.findById(id)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }

    @GetMapping("/me")
    public ResponseEntity<MyUserFullResponse> getMyProfile(Authentication authentication) {
        log.info("GET /api/users/me={}", authentication.getName());
        // authentication.getName() = username (bei JWT sub=username)
        String username = authentication.getName();
        return userService.getMyProfile(username)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }

    @PatchMapping("/me")
    public ResponseEntity<MyUserFullResponse> updateMyProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateMyProfileRequest request
    ) {
        log.info("Patch /api/users/me={}", authentication.getName());
        String username = authentication.getName();
        return userService.updateMyProfile(username, request).map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound()
                                                .build());
    }
}
