package de.jarovart.freemoment.server.model.dtos.requests;

public record ResetPasswordRequest(
        String token,
        String newPassword
) {
}
