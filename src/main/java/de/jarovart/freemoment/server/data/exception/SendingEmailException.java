package de.jarovart.freemoment.server.data.exception;

public class SendingEmailException extends RuntimeException {
  public SendingEmailException(String message) {
    super(message);
  }
}
