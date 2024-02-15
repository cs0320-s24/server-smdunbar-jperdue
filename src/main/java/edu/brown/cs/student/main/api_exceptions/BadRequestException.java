package edu.brown.cs.student.main.api_exceptions;

public class BadRequestException extends Exception {
  /** errors with the bad requests */
  public BadRequestException(String message) {
    super(message);
  }
}
