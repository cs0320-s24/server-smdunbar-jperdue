package edu.brown.cs.student.main.api_exceptions;

public class DatasourceException extends Exception {

  /**
   * errors within the datasource
   *
   * @param message message to be shown to users
   */
  public DatasourceException(String message) {
    super(message);
  }
}
