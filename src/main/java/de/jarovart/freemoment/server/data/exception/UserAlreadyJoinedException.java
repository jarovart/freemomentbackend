package de.jarovart.freemoment.server.data.exception;

public class UserAlreadyJoinedException extends RuntimeException {

    public UserAlreadyJoinedException(String message) {
        super(message);
    }
}
