package de.jarovart.freemoment.server.model.dtos.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatusFullResponse extends StatusResponse {
    @NotNull
    private Boolean dataBaseOnline;
    private String serverVersion;

    public StatusFullResponse(Boolean serverOnline, Boolean dataBaseOnline, String serverVersion) {
        super(serverOnline);
        this.dataBaseOnline = dataBaseOnline;
        this.serverVersion = serverVersion;
    }
}
