package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationResponse {

    private static int likedTest = 0;
    @NotNull
    private Long id;
    private String title;
    private String description;
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
    private Long createdUserId;
    private String createdUsername;
    private int joinedUserCount;
    private int likedUserCount;

    public LocationResponse(Long id, String title, String description, LocalDateTime creationDateTime,
                            LocalDateTime startDateTime, LocalDateTime endDateTime, Double latitude, Double longitude,
                            String thumbnailUrl, Long createdUserId, String createdUsername, int joinedUserCount,
                            int likedUserCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDateTime = creationDateTime;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.thumbnailUrl = thumbnailUrl;
        this.createdUserId = createdUserId;
        this.createdUsername = createdUsername;
        this.joinedUserCount = joinedUserCount;
        this.likedUserCount = likedTest++;
    }
}
