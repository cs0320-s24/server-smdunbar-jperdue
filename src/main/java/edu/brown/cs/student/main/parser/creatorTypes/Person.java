package edu.brown.cs.student.main.parser.creatorTypes;

import edu.brown.cs.student.main.parser.CreatorFromRow;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import java.util.List;

public class Person implements CreatorFromRow<Person> {

  // Example of a class for testing purposes

  private String firstName;

  private String lastName;

  private int age;

  /**
   * Create new person object
   *
   * @param firstName String first name
   * @param lastName String last name
   * @param age integer age
   */
  public Person(String firstName, String lastName, int age) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
  }

  /**
   * Create a Person objects from a input row.
   *
   * @param row List of Strings
   * @return Person object
   * @throws FactoryFailureException If row size is incorrect or age is formatted in a form other
   *     than numerical character (ex. 1,2,3,4...)
   */
  @Override
  public Person create(List<String> row) throws FactoryFailureException {
    if (row.size() == 3) {
      try {
        return new Person(row.get(0), row.get(1), Integer.parseInt(row.get(2)));
      } catch (NumberFormatException e) {
        throw new FactoryFailureException("Age Formatted Incorrectly", row);
      }
    } else {
      throw new FactoryFailureException("Incorrect Row Size", row);
    }
  }

  /**
   * Getter method for testing purposes.
   *
   * @return int age
   */
  public int getAge() {
    return this.age;
  }

  /**
   * Getter method for testing purposes.
   *
   * @return String first name
   */
  public String getFirstName() {
    return this.firstName;
  }
}
