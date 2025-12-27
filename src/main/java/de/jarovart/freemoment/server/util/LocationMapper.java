package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.dtos.AppUserBaseDTO;
import de.jarovart.freemoment.server.model.dtos.LocationBaseDTO;
import de.jarovart.freemoment.server.model.dtos.LocationCreateDTO;
import de.jarovart.freemoment.server.model.dtos.LocationFullDTO;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;

import java.util.List;

/**
 * Überlege auf MapStruct zu wechseln. Ab +10 DTOs.
 */
public class LocationMapper {
    public static List<LocationBaseDTO> toBaseDTOs(List<Location> locations) {
        return locations.stream().map(LocationMapper::toBaseDTO).toList();
    }

    public static LocationBaseDTO toBaseDTO(Location location) {
        return new LocationBaseDTO(
                location.getId(),
                location.getTitle(),
                location.getDescription(),
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
     * @return @{@link LocationFullDTO} ready for sending.
     */
    public static LocationFullDTO toFullDTO(Location location) {
        List<AppUserBaseDTO> likedByUsers = location
                .getLikedByUsers()
                .stream()
                .map(user -> new AppUserBaseDTO(user.getId(), user.getUsername()))
                .toList();
        List<AppUserBaseDTO> joinedUsers = location
                .getJoinedUsers().stream()
                .map(user -> new AppUserBaseDTO(user.getId(), user.getUsername()))
                .toList();
        return new LocationFullDTO(location.getId(),
                                   location.getTitle(),
                                   location.getDescription(),
                                   location.getCreationDateTime(),
                                   location.getStartDateTime(),
                                   location.getEndDateTime(),
                                   location.getLatitude(),
                                   location.getLongitude(),
                                   location.getThumbnailUrl(),
                                   location.getImageUrl(),
                                   location.getCreatedUser().getId(),
                                   location.getCreatedUser().getUsername(),
                                   joinedUsers,
                                   likedByUsers);
    }

    public static Location fromCreateDTO(LocationCreateDTO locationCreateDTO, AppUser user) {
        return new Location(
                locationCreateDTO.getTitle(),
                locationCreateDTO.getDescription(),
                locationCreateDTO.getCreationDateTime(),
                locationCreateDTO.getStartDateTime(),
                locationCreateDTO.getEndDateTime(),
                locationCreateDTO.getLatitude(),
                locationCreateDTO.getLongitude(),
                locationCreateDTO.getThumbnailUrl(),
                "",
                user
        );
    }
}
