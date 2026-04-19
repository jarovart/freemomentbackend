package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.response.NominatimResultResponse;
import de.jarovart.freemoment.server.model.dtos.response.PlaceResponse;
import de.jarovart.freemoment.server.model.entities.Place;
import de.jarovart.freemoment.server.repository.PlaceRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.services.OsmGeocodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private OsmGeocodingService osmGeocodingService;
    @Autowired
    private UserRepository userRepository;

    public List<PlaceResponse> getPlacesByQuery(String placeNameQuery, Long userId) {
        String filteredQuery = placeNameQuery == null ? "" : placeNameQuery.trim();

        if (filteredQuery.length() < 4) {
            return Collections.emptyList();
        }
        List<Place> dbPlaces = placeRepository.findPlaceSuggestions(filteredQuery, PageRequest.of(0, 5));

        System.out.println(dbPlaces);
        if (!dbPlaces.isEmpty()) {
            return dbPlaces.stream().map(place -> placeToResponse(place, true)).toList();
        }
        return osmSearch(filteredQuery, userId);
    }


    @Transactional
    public List<PlaceResponse> osmSearch(String query, Long userId) {
        List<NominatimResultResponse> osmList = osmGeocodingService.searchPlaces(query);
        if (osmList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Place> placesOsm = new ArrayList<>();
        for (NominatimResultResponse osm : osmList) {
            String name = osm.display_name();
            try {
                Place place = placeRepository.findByName(name).orElse(new Place());
                place.setName(name);
                place.setLatitude(osm.lat());
                place.setLongitude(osm.lon());
                place.setCreatedAt(LocalDateTime.now());
                if (userId != null) {
                    place.setCreatorUser(userRepository.getReferenceById(userId));
                }
                placesOsm.add(placeRepository.save(place));
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
            }
        }

        return placesOsm.stream().map(place -> placeToResponse(place, false)).toList();
    }

    public PlaceResponse placeToResponse(Place place, boolean fromDb) {

        System.out.println(place);
        return new PlaceResponse(place.getId(), place.getName(), place.getLatitude(), place.getLongitude(), fromDb);
    }
}
