package de.jarovart.freemoment.server.model.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageRequest {
    private Long id;
    private Boolean isNew;
    private String clientKey;
}
