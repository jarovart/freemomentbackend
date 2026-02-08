package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.dtos.requests.LocationCreateRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.exception.UserAlreadyJoinedException;
import de.jarovart.freemoment.server.model.exception.UserNotLeavedException;
import de.jarovart.freemoment.server.repository.LocationRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.util.LocationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    public List<LocationResponse> getAllLocations(int limit) {
        Pageable pageable = PageRequest.of(0, Math.min(limit, 500));
        return LocationMapper.toLocationResponse(locationRepository.findAll(pageable).getContent());
    }

    public List<LocationResponse> getLocationsWithinBounds(double minLat, double maxLat, double minLng, double maxLng) {
        if (isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 200); // 🔥 Max Marker
        return LocationMapper.toLocationResponse(
                locationRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng, limit));
    }

    public List<LocationResponse> search(String query) {
        if (query == null || query.isBlank() || query.length() < 3) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 10);
        return LocationMapper.toLocationResponse(
                locationRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, limit)
                                  .getContent());
    }

    public Optional<LocationFullResponse> getLocationById(Long id) {
        Optional<Location> location = locationRepository.findByIdWithUsers(id);
        return location.map(LocationMapper::toFullResponse);
    }

    @Transactional
    public LocationResponse createLocation(LocationCreateRequest locationCreateRequest, String username) {
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Location location = LocationMapper.fromCreateRequest(locationCreateRequest, user);
        return LocationMapper.toLocationResponse(locationRepository.save(location));
    }

    public List<LocationResponse> getLocationsWithinBoundsAndRange(
            double minLat, double maxLat, double minLng, double maxLng, LocalDateTime rangeStart,
            LocalDateTime rangeEnd) {
        if (rangeStart == null || rangeEnd == null || !(rangeStart.isBefore(rangeEnd) || rangeStart.isEqual(rangeEnd))
                || isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 200); // z.B. max Marker
        return LocationMapper.toLocationResponse(
                locationRepository.findWithinBoundsAndOverlappingRange(minLat, maxLat, minLng, maxLng, rangeStart,
                                                                       rangeEnd, limit));
    }

    @Transactional
    public void joinLocation(Long locationId, String username) {
        Location location = locationRepository.findById(locationId)
                                              .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!location.getJoinedUsers().add(user)) {
            throw new UserAlreadyJoinedException("User already joined this location");
        }
    }

    @Transactional
    public void leaveLocation(Long locationId, String username) {
        Location location = locationRepository.findById(locationId)
                                              .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (!location.getJoinedUsers().remove(user)) {
            throw new UserNotLeavedException("User has not been removed from this location");
        }
    }

    private boolean isInvalidBounds(double minLat, double maxLat, double minLng, double maxLng) {
        return !(minLat >= -90) || !(maxLat <= 90) || !(minLng >= -180) || !(maxLng <= 180) || !(minLat < maxLat)
                || !(minLng < maxLng);
    }

    public Slice<LocationResponse> getSliceLocationsByFilterSettings(
            int page, int paramSize, String query, double lat, double lng, double radiusKm, LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        int size = Math.min(paramSize, 50);
        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / (111.0 * Math.cos(Math.toRadians(lat)));
        double minLat = lat - latDelta;
        double maxLat = lat + latDelta;
        double minLng = lng - lngDelta;
        double maxLng = lng + lngDelta;

        var pageable = PageRequest.of(page, size * 3,
                                      Sort.by(Sort.Order.desc("creationDateTime"), Sort.Order.desc("id")));

        var pageResult = locationRepository.searchH2(startDateTime, endDateTime, minLat, maxLat, minLng, maxLng, query,
                                                     pageable);
        var filtered = pageResult.getContent().stream()
                                 .filter(l -> haversineKm(lat, lng, l.getLatitude(), l.getLongitude()) <= radiusKm)
                                 .limit(size)
                                 .map(LocationMapper::toLocationResponse)
                                 .toList();

        boolean hasNext = pageResult.hasNext() || pageResult.getNumberOfElements() > size;
        return new SliceImpl<>(filtered, PageRequest.of(page, size), hasNext);
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0088;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.asin(Math.sqrt(a));
    }

}
