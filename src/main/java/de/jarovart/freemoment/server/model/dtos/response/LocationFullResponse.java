package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class LocationFullResponse {
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
    private ImageResponse thumbnailImage;
    private Set<ImageResponse> images;
    private Long createdUserId;
    private String createdUsername;
    private long likedUserCount;
    private long joinedUserCount;
    private Boolean likedByCurrentUser;
    private Boolean joinedByCurrentUser;

    public LocationFullResponse(Long id, String title, String description, String address,
                                LocalDateTime creationDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime,
                                Double latitude, Double longitude, ImageResponse thumbnailImage,
                                Set<ImageResponse> images, Long createdUserId, String createdUsername,
                                long likedUserCount, long joinedUserCount, Boolean likedByCurrentUser,
                                Boolean joinedByCurrentUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.address = address;
        this.creationDateTime = creationDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailImage = thumbnailImage;
        this.images = images;
        this.createdUserId = createdUserId;
        this.createdUsername = createdUsername;
        this.likedUserCount = likedUserCount;
        this.joinedUserCount = joinedUserCount;
        this.likedByCurrentUser = likedByCurrentUser;
        this.joinedByCurrentUser = joinedByCurrentUser;

    }
}
