package de.jarovart.freemoment.server.model.exception;

public class UserAlreadyJoinedException extends RuntimeException {

    public UserAlreadyJoinedException(String message) {
        super(message);
    }
}
