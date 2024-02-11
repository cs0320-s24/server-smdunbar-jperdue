package edu.brown.cs.student.main.search;

/**
 * Exception used to catch any errors created when searching through a CSV. This could include
 * indexing by headers when no headers are included, or providing a column index out of bounds of
 * the data.
 */
public class InvalidQueryException extends Exception {

  public InvalidQueryException(String message) {
    super(message);
  }
}
