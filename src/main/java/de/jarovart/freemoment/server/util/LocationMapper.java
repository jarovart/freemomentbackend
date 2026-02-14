package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.dtos.requests.LocationCreateRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Überlege auf MapStruct zu wechseln. Ab +10 DTOs.
 */
public class LocationMapper {
    public static List<LocationResponse> toLocationResponse(List<Location> locations) {
        return locations.stream().map(LocationMapper::toLocationResponse).toList();
    }

    public static Set<LocationResponse> toLocationResponseSet(Set<Location> locations) {
        return locations.stream().map(LocationMapper::toLocationResponse).collect(Collectors.toSet());
    }

    public static LocationResponse toLocationResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getTitle(),
                location.getDescription(),
                location.getAddress(),
                location.getCreationDateTime(),
                location.getStartDateTime(),
                location.getEndDateTime(),
                location.getLatitude(),
                location.getLongitude(),
                location.getThumbnailUrl(),
                location.getCreatedUser().getId(),
                location.getCreatedUser().getUsername(),
                location.getJoinedUsers().size(),
                location.getLikedByUsers().size());
    }

    /**
     * Careful: Lazy loading exist (likedByUsers, joinedUsers) - transacational read only is needed
     *
     * @param location entity to parse into full dto.
     * @return @{@link LocationFullResponse} ready for sending.
     */
    public static LocationFullResponse toFullResponse(Location location) {
        List<UserResponse> likedByUsers = location
                .getLikedByUsers()
                .stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getFirstName(),
                                              user.getLastName(), user.getProfileUrl()))
                .toList();
        List<UserResponse> joinedUsers = location
                .getJoinedUsers().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getFirstName(),
                                              user.getLastName(), user.getProfileUrl()))
                .toList();
        return new LocationFullResponse(location.getId(),
                                        location.getTitle(),
                                        location.getDescription(),
                                        location.getAddress(),
                                        location.getCreationDateTime(),
                                        location.getStartDateTime(),
                                        location.getEndDateTime(),
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        location.getThumbnailUrl(),
                                        location.getImageUrls(),
                                        location.getCreatedUser().getId(),
                                        location.getCreatedUser().getUsername(),
                                        joinedUsers,
                                        likedByUsers);
    }

    public static Location fromCreateRequest(LocationCreateRequest locationCreateRequest, AppUser user) {
        return new Location(
                locationCreateRequest.getTitle(),
                locationCreateRequest.getDescription(),
                locationCreateRequest.getAddress(),
                locationCreateRequest.getCreationDateTime(),
                locationCreateRequest.getStartDateTime(),
                locationCreateRequest.getEndDateTime(),
                locationCreateRequest.getLatitude(),
                locationCreateRequest.getLongitude(),
                locationCreateRequest.getThumbnailUrl(),
                locationCreateRequest.getImageUrls(),
                user
        );
    }
}
