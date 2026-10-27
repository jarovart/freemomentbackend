package de.jarovart.freemoment.server.model.dtos.response;

import java.time.LocalDateTime;

public record LastMailRecentlySendExceptionResponse(
        String code,
        int status,
        String message,
        LocalDateTime timestamp,
        long cooldownSeconds
) {
}
