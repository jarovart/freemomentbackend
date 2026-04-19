package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.dtos.response.NominatimResultResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class OsmGeocodingService {

    private final static String OSM_URL = "https://nominatim.openstreetmap.org/search"
            + "?q={0}&format=jsonv2&limit=5&addressdetails=1";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<NominatimResultResponse> searchPlaces(String placeQuery) {
        String query = placeQuery == null ? "" : placeQuery.trim();
        if (query.length() < 4) {
            return Collections.emptyList();
        }

        String url = MessageFormat.format(OSM_URL, UriUtils.encode(query, StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "meetmaap/1.0 (contact: artem.jarovoj@jarovart.com)");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<NominatimResultResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NominatimResultResponse[].class
        );

        if (response.getBody() == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(response.getBody()).toList();
    }
}