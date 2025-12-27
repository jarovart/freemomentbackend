package de.jarovart.freemoment.server.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LastMailRecentlySendException extends RuntimeException {

    private final HttpStatus status;
    private final long cooldownSeconds;

    public LastMailRecentlySendException(HttpStatus status, long cooldownSeconds, String message) {
        super(message);
        this.status = status;
        this.cooldownSeconds = cooldownSeconds;
    }

}
