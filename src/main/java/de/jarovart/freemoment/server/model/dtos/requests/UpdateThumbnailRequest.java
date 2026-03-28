package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateThumbnailRequest {

    @NotBlank
    private Long imageId;
}
