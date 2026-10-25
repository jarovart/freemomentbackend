package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SettingsRequest {
    private String languageCode;
    private String design;
    @NotNull
    private LocalDateTime updatedAt;

    public SettingsRequest(String locale, String design, LocalDateTime updatedAt) {
        this.languageCode = languageCode;
        this.design = design;
        this.updatedAt = updatedAt;
    }
}
