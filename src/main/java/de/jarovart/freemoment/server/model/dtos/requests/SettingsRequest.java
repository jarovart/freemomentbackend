package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SettingsRequest {
    private String locale;
    private String design;
    @NotNull
    private LocalDateTime updatedAt;

    public SettingsRequest(String locale, String design, LocalDateTime updatedAt) {
        this.locale = locale;
        this.design = design;
        this.updatedAt = updatedAt;
    }
}
