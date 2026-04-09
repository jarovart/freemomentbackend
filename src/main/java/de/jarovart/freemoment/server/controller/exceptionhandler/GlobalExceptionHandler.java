package de.jarovart.freemoment.server.controller.exceptionhandler;

import de.jarovart.freemoment.server.model.dtos.response.ErrorResponse;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceResponseException.class)
    public ResponseEntity<ErrorResponse> handleServiceResponseException(ServiceResponseException ex) {
        System.out.println("HANDLER HIT");
        System.out.println("status = " + ex.getStatus());
        System.out.println("message = " + ex.getMessage());
        System.out.println("errorCode = " + ex.getErrorCode());

        ErrorResponse response = new ErrorResponse(
                ex.getErrorCode() != null ? ex.getErrorCode().name() : null,
                ex.getMessage(),
                ex.getStatus().value(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(response);
    }
}
