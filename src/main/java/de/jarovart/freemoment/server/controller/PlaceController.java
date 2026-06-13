package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.response.PlaceResponse;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.services.controllerservices.PlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author Artem
 */
@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private static final Logger log =
            LoggerFactory.getLogger(PlaceController.class);
    @Autowired
    private PlaceService placeService;

    @GetMapping()
    public ResponseEntity<List<PlaceResponse>> findByQuery(@RequestParam("query") String placeNameQuery,
                                                           @AuthenticationPrincipal JarovartUserDetails user) {
        log.info("GET /api/places?query={} places wurde aufgerufen", placeNameQuery);
        Long userId = user != null ? user.getId() : null;
        List<PlaceResponse> placeResponses = placeService.getPlacesByQuery(placeNameQuery, userId);
        return ResponseEntity.ok(placeResponses);
    }
}
