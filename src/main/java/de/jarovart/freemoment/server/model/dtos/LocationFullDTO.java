package de.jarovart.freemoment.server.model.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LocationFullDTO {
    @NotNull
    private Long id;
    private String title;
    private String description;
    private String address;
    @NotNull
    private LocalDateTime creationDateTime;
    @NotNull
    private LocalDateTime startDateTime;
    @NotNull
    private LocalDateTime endDateTime;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
    private String thumbnailUrl;
    private List<String> imageUrls;
    private Long createdUserId;
    private String createdUsername;
    private List<AppUserBaseDTO> joinedUsers;
    private List<AppUserBaseDTO> likedUsers;

    public LocationFullDTO(Long id, String title, String description, String address, LocalDateTime creationDateTime,
                           LocalDateTime startDateTime, LocalDateTime endDateTime, Double latitude,
                           Double longitude, List<String> imageUrls, Long createdUserId, String createdUsername,
                           List<AppUserBaseDTO> joinedUsers, List<AppUserBaseDTO> likedUsers) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailUrl = thumbnailUrl;
        this.imageUrls = imageUrls;
        this.createdUserId = createdUserId;
        this.createdUsername = createdUsername;
        this.joinedUsers = joinedUsers;
        this.likedUsers = likedUsers;

    }
}
