package de.jarovart.freemoment.server.controller.exceptionhandler;

import de.jarovart.freemoment.server.model.dtos.response.LastMailRecentlySendExceptionResponse;
import de.jarovart.freemoment.server.model.exception.LastMailRecentlySendException;
import de.jarovart.freemoment.server.model.exception.SendingEmailException;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(ServiceResponseException.class)
    public ResponseEntity<String> handle(ServiceResponseException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(SendingEmailException.class)
    public ResponseEntity<String> handle(SendingEmailException e) {
        return ResponseEntity.status(e.getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(LastMailRecentlySendException.class)
    public ResponseEntity<LastMailRecentlySendExceptionResponse> handle(LastMailRecentlySendException e) {
        return ResponseEntity.status(e.getStatus())
                             .body(new LastMailRecentlySendExceptionResponse(e.getMessage(), e.getCooldownSeconds()));
    }
}
