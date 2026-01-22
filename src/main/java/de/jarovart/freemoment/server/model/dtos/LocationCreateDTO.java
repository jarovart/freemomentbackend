package de.jarovart.freemoment.server.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LocationCreateDTO {

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

    private List<String> imageUrls;
    @NotNull
    private String createdUsername;
}
