package de.jarovart.freemoment.server.model.dtos.response;

import lombok.Data;

@Data
public class AppUserResponse {
    private Long id;
    private String username;

    public AppUserResponse(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
