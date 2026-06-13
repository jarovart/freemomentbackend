package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SettingsResponse {
    private Long id;
    private String locale;
    private String design;
    @NotNull
    private LocalDateTime updatedAt;

    public SettingsResponse(Long id, String locale, String design, LocalDateTime updatedAt) {
        this.id = id;
        this.locale = locale;
        this.design = design;
        this.updatedAt = updatedAt;
    }
}
