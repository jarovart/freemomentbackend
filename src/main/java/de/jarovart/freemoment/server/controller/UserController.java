package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
}
