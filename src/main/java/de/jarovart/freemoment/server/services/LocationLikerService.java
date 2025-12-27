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
public class LocationLikerService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void likeLocation(Long locationId, String username) {
        Location location = locationRepository.findById(locationId)
                                              .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        location.getLikedByUsers().add(user);
    }

    @Transactional
    public void unlikeLocation(Long locationId, String username) {
        Location location = locationRepository.findById(locationId)
                                              .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        location.getLikedByUsers().remove(user);
    }
}
