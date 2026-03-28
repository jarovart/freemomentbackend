package de.jarovart.freemoment.server.services;


import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.enums.LocationType;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserLocationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationService locationService;

    public List<LocationResponse> getLocationsByUserId(Long targetUserId, LocationType locationType,
                                                       String requesterUsername) {
        AppUser targetUser = userRepository.findById(targetUserId)
                                           .orElseThrow(() -> new ServiceResponseException(
                                                   HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        if (requesterUsername == null || requesterUsername.isBlank()) {
            throw new ServiceResponseException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
        }
        boolean isOwnProfile = targetUser.getUsername().equals(requesterUsername);
        return getLocationsFromUser(targetUserId, locationType, isOwnProfile);
    }

    public List<LocationResponse> getMyLocations(LocationType locationType, String username) {
        AppUser me = userRepository.findByUsername(username)
                                   .orElseThrow(() -> new ServiceResponseException(
                                           HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        return getLocationsFromUser(me.getId(), locationType, true);
    }

    private List<LocationResponse> getLocationsFromUser(Long targetUserId, LocationType locationType,
                                                        boolean isOwnProfile) {
        if (!isOwnProfile && locationType == LocationType.LIKED) {
            throw new ServiceResponseException(HttpStatus.FORBIDDEN, "LIKED_LOCATIONS_NOT_PUBLIC");
        }

        return switch (locationType) {
            case CREATED -> locationService.findCreatedUserLocations(targetUserId);
            case LIKED -> locationService.findLikedUserLocations(targetUserId);
            case JOINED -> locationService.findJoinedUserLocations(targetUserId);
        };
    }
}
