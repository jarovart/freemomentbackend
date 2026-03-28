package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.repository.LocationRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class LocationJoiningService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean hasUserJoined(Long locationId, Long userId) {
        return locationRepository.existsByIdAndJoinedUsers_Id(locationId, userId);
    }

    @Transactional
    public void joinLocation(Long locationId, Long userId) {
        Location location = locationRepository.findById(locationId)
                                              .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        AppUser user = userRepository.findById(userId)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        location.getJoinedUsers().add(user);
    }

    @Transactional
    public void unjoinLocation(Long locationId, Long userId) {
        Location location = locationRepository.findById(locationId)
                                              .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        AppUser user = userRepository.findById(userId)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        location.getJoinedUsers().remove(user);
    }
}
