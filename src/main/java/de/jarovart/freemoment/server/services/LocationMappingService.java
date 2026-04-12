package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.dtos.response.LocationFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.util.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationMappingService {

    @Autowired
    private LocationLikerService locationLikerService;
    @Autowired
    private LocationJoiningService locationJoiningService;

    public LocationResponse mapToLocationResponse(Location location, Long userId) {
        long locationId = location.getId();
        long countLiked = locationLikerService.getCountByLocationId(locationId);
        long countJoined = locationJoiningService.getCountByLocationId(locationId);
        Boolean likedByCurrentUser = userId == null ? null : locationLikerService.hasUserLiked(locationId, userId);
        Boolean joinedByCurrentUser = userId == null ? null : locationJoiningService.hasUserJoined(locationId, userId);
        return LocationMapper.toLocationResponse(location, countLiked, countJoined, likedByCurrentUser,
                                                 joinedByCurrentUser);
    }

    public LocationFullResponse mapToLocationFullResponse(Location location, Long userId) {
        long locationId = location.getId();
        long countLiked = locationLikerService.getCountByLocationId(locationId);
        long countJoined = locationJoiningService.getCountByLocationId(locationId);
        Boolean likedByCurrentUser = userId == null ? null : locationLikerService.hasUserLiked(locationId, userId);
        Boolean joinedByCurrentUser = userId == null ? null : locationJoiningService.hasUserJoined(locationId, userId);
        return LocationMapper.toLocationFullResponse(location, countLiked, countJoined, likedByCurrentUser,
                                                     joinedByCurrentUser);
    }
}
