package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateMyLocationRequest {

    @NotNull
    private Long id;
    @NotBlank
    private String title;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    @NotNull
    private LocalDateTime startDateTime;
    @NotNull
    private LocalDateTime endDateTime;
    //private MultipartFile profileImage;
}