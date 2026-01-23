package de.jarovart.freemoment.server.model.dtos.response;

public record LoginResponse(
        String token,
        String username
) {
}
