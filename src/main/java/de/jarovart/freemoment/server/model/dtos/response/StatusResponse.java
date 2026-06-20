package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusResponse {
    @NotNull
    private Boolean serverOnline;

    private StatusResponse() {
    }

    public StatusResponse(Boolean serverOnline) {
        this.serverOnline = serverOnline;
    }
}