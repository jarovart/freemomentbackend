package de.jarovart.freemoment.server.model.dtos.requests;

public record RegisterRequest(
        String username,
        String firstname,
        String lastname,
        String email,
        String password
) {
}
