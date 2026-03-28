package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationCreateRequest {

    @NotBlank
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
    @NotNull
    private String createdUsername;
}
