package de.jarovart.freemoment.server.model.dtos.response;

public record LastMailRecentlySendExceptionResponse(
        String message,
        long secondsUntilNextMailAllowed
) {
}
