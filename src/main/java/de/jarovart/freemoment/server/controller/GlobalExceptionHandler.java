package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.data.dtos.ApiError;
import de.jarovart.freemoment.server.data.exception.UserAlreadyJoinedException;
import de.jarovart.freemoment.server.data.exception.UserNotLeavedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ApiError(404, "NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyJoinedException.class)
    public ResponseEntity<ApiError> handleAlreadyJoined(UserAlreadyJoinedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ApiError(409, "ALREADY_JOINED", ex.getMessage()));
    }

    @ExceptionHandler(UserNotLeavedException.class)
    public ResponseEntity<ApiError> handleNotJoined(UserNotLeavedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                             .body(new ApiError(409, "NOT_JOINED", ex.getMessage()));
    }
}
