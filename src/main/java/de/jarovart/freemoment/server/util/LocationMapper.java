package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.dtos.requests.LocationCreateRequest;
import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyLocationRequest;
import de.jarovart.freemoment.server.model.dtos.response.ImageResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Image;
import de.jarovart.freemoment.server.model.entities.Location;

import java.util.Comparator;
import java.util.List;

/**
 * Überlege auf MapStruct zu wechseln. Ab +10 DTOs.
 */
public class LocationMapper {

    public static LocationResponse toLocationResponse(Location location, long countLikedUsers, long countJoinedUsers,
                                                      Boolean likedByCurrentUser, Boolean joinedByCurrentUser) {
        ImageResponse thumbnailResponse = null;
        if (location.getThumbnailImage() != null) {
            thumbnailResponse = new ImageResponse(location.getThumbnailImage().getId(),
                                                  location.getThumbnailImage().getUrl());
        }

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
                thumbnailResponse,
                location.getCreatedUser().getId(),
                location.getCreatedUser().getUsername(),
                countLikedUsers,
                countJoinedUsers,
                likedByCurrentUser,
                joinedByCurrentUser);
    }

    /**
     * Careful: Lazy loading exist (likedByUsers, joinedUsers) - transactional read only is needed
     *
     * @param location entity to parse into full dto.
     * @return @{@link LocationFullResponse} ready for sending.
     */
    public static LocationFullResponse toLocationFullResponse(Location location, long countLikedUsers,
                                                              long countJoinedUsers,
                                                              Boolean likedByCurrentUser, Boolean joinedByCurrentUser) {
        ImageResponse thumbnailResponse = null;
        if (location.getThumbnailImage() != null) {
            thumbnailResponse = ImageMapper.toImageResponse(location.getThumbnailImage());
        }
        List<ImageResponse> imageResponses = location.getImages().stream()
                                                     .sorted(Comparator.comparing(Image::getSortIndex))
                                                     .map(ImageMapper::toImageResponse)
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
                                        thumbnailResponse,
                                        imageResponses,
                                        location.getCreatedUser().getId(),
                                        location.getCreatedUser().getUsername(),
                                        countLikedUsers,
                                        countJoinedUsers,
                                        likedByCurrentUser,
                                        joinedByCurrentUser);
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
                user
        );
    }

    public static Location fromUpdateRequest(Location location, UpdateMyLocationRequest updateMyLocationRequest) {
        location.setTitle(updateMyLocationRequest.getTitle());
        location.setDescription(updateMyLocationRequest.getDescription());
        location.setAddress(updateMyLocationRequest.getAddress());
        location.setLatitude(updateMyLocationRequest.getLatitude());
        location.setLongitude(updateMyLocationRequest.getLongitude());
        location.setStartDateTime(updateMyLocationRequest.getStartDateTime());
        location.setEndDateTime(updateMyLocationRequest.getEndDateTime());
        return location;
    }
}
