package de.jarovart.freemoment.server.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    String code;
    String message;
    int status;
    LocalDateTime timestamp;
}
