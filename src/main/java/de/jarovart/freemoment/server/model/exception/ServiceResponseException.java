package de.jarovart.freemoment.server.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceResponseException extends RuntimeException {

    private final HttpStatus status;

    public ServiceResponseException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

}
