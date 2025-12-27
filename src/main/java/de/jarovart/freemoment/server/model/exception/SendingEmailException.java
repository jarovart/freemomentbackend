package de.jarovart.freemoment.server.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SendingEmailException extends RuntimeException {

    private final HttpStatus status;

    public SendingEmailException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
