package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.response.StatusResponse;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.controllerservices.InfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoController {

    private static final Logger log =
            LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private InfoService infoService;

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> getServerInfo() {
        log.info("GET /api/info/status");
        return ResponseEntity.ok(infoService.getServerInfo());
    }

    @GetMapping("/fullStatus")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StatusResponse> getFullServerInfo(
            @AuthenticationPrincipal JarovartUserDetails userDetails) {
        log.info("GET /api/info/fullStatus={}", userDetails.getUsername());
        return ResponseEntity.ok(infoService.getFullServerInfo(userDetails.getId()));
    }
}
