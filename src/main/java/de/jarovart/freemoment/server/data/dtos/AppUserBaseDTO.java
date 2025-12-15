package de.jarovart.freemoment.server.data.dtos;

import lombok.Data;

@Data
public class AppUserBaseDTO {
    private Long id;
    private String username;

    public AppUserBaseDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
