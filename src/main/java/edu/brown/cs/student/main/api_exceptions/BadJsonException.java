package edu.brown.cs.student.main.api_exceptions;

public class BadJsonException extends Exception {

  /**
   * when there is a bad json
   *
   * @param message message to be shown to user
   */
  public BadJsonException(String message) {
    super(message);
  }
}
