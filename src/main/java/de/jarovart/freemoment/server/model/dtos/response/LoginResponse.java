package de.jarovart.freemoment.server.model.dtos.response;

public record LoginResponse(
        Long userId,
        String username,
        String myProfileUrl,
        String token,
        String refreshToken
) {
}
