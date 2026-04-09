package de.jarovart.freemoment.server.model.exception;

import de.jarovart.freemoment.server.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceResponseException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;

    public ServiceResponseException(HttpStatus status, String message, ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
