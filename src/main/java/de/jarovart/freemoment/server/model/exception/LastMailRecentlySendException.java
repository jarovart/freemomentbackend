package de.jarovart.freemoment.server.model.exception;

import de.jarovart.freemoment.server.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class LastMailRecentlySendException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final long cooldownSeconds;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public LastMailRecentlySendException(HttpStatus status, long cooldownSeconds, String message, ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.cooldownSeconds = cooldownSeconds;
        this.errorCode = errorCode;
    }
}
