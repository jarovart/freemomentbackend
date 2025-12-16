package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.data.dtos.LocationBaseDTO;
import de.jarovart.freemoment.server.data.dtos.LocationCreateDTO;
import de.jarovart.freemoment.server.data.dtos.LocationFullDTO;
import de.jarovart.freemoment.server.data.entities.AppUser;
import de.jarovart.freemoment.server.data.entities.Location;
import de.jarovart.freemoment.server.data.exception.UserAlreadyJoinedException;
import de.jarovart.freemoment.server.data.exception.UserNotLeavedException;
import de.jarovart.freemoment.server.repository.LocationRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.util.LocationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    public List<LocationBaseDTO> getAllLocations(int limit) {
        Pageable pageable = PageRequest.of(0, Math.min(limit, 500));
        return LocationMapper.toBaseDTOs(locationRepository.findAll(pageable).getContent());
    }

    public List<LocationBaseDTO> getLocationsWithinBounds(double minLat, double maxLat, double minLng, double maxLng) {
        if (isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 200); // 🔥 Max Marker
        return LocationMapper.toBaseDTOs(
                locationRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng, limit));
    }

    public List<LocationBaseDTO> search(String query) {
        if (query == null || query.isBlank() || query.length() < 3) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 10);
        return LocationMapper.toBaseDTOs(
                locationRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, limit)
                                  .getContent());
    }

    @Transactional(readOnly = true)
    public Optional<LocationFullDTO> getLocationById(Long id) {
        Optional<Location> location = locationRepository.findByIdWithUsers(id);
        return location.map(LocationMapper::toFullDTO);
    }

    @Transactional
    public LocationBaseDTO createLocation(LocationCreateDTO locationCreateDTO, String username) {
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Location location = LocationMapper.fromCreateDTO(locationCreateDTO, user);
        return LocationMapper.toBaseDTO(locationRepository.save(location));
    }

    public List<LocationBaseDTO> getLocationsWithinBoundsAndRange(
            double minLat, double maxLat, double minLng, double maxLng, LocalDateTime rangeStart,
            LocalDateTime rangeEnd) {
        if (rangeStart == null || rangeEnd == null || !(rangeStart.isBefore(rangeEnd) || rangeStart.isEqual(rangeEnd))
                || isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 200); // z.B. max Marker
        return LocationMapper.toBaseDTOs(
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
}
