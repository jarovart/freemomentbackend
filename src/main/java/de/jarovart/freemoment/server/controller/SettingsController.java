package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.requests.SettingsRequest;
import de.jarovart.freemoment.server.model.dtos.response.SettingsResponse;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.controllerservices.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private static final Logger log =
            LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/me")
    public ResponseEntity<SettingsResponse> getMySettings(@AuthenticationPrincipal JarovartUserDetails userDetails) {
        log.info("GET /api/settings/me={}", userDetails.getUsername());
        return ResponseEntity.ok(settingsService.getMySettings(userDetails.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<SettingsResponse> saveMySettings(@AuthenticationPrincipal JarovartUserDetails userDetails,
                                                           @RequestBody SettingsRequest settingsRequest) {
        log.info("PUT /api/settings/me={}", userDetails.getUsername());
        return ResponseEntity.ok(settingsService.saveMySettings(settingsRequest, userDetails.getId()));
    }
}
