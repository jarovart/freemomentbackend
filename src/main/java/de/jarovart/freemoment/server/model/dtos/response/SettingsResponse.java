package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SettingsResponse {
    private Long id;
    private String languageCode;
    private String design;
    @NotNull
    private LocalDateTime updatedAt;

    public SettingsResponse(Long id, String languageCode, String design, LocalDateTime updatedAt) {
        this.id = id;
        this.languageCode = languageCode;
        this.design = design;
        this.updatedAt = updatedAt;
    }
}
