package de.jarovart.freemoment.server.data.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationCreateDTO {

    @NotBlank
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
    private String imageUrl;
    @NotNull
    private Long createdUserId;
    @NotNull
    private String createdUsername;
}
