package de.jarovart.freemoment.server.model.dtos.requests;

public record LoginRequest(
        String username,
        String password
) {
}
