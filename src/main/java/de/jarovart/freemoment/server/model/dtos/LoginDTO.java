package de.jarovart.freemoment.server.model.dtos;

public record LoginDTO(
        String token,
        String username
) {
}
