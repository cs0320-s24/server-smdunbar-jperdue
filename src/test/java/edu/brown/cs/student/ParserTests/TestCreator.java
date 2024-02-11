package edu.brown.cs.student.ParserTests;

import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.parser.creatorTypes.Person;
import edu.brown.cs.student.main.parser.creatorTypes.Star;
import java.util.List;
import org.junit.Test;
import org.testng.Assert;

public class TestCreator {

  /**
   * Tests a valid creation of a person given a row.
   *
   * @throws FactoryFailureException if input row is invalid
   */
  @Test
  public void testValidPersonCreate() throws FactoryFailureException {
    Person person = new Person("John", "Doe", 35);
    List<String> validPerson = List.of("John", "Doe", "35");

    Assert.assertEquals(person.create(validPerson).getFirstName(), person.getFirstName());
    Assert.assertEquals(person.create(validPerson).getAge(), person.getAge());
  }

  /**
   * Tests exception thrown when list is too large when creating a person
   *
   * @throws FactoryFailureException if list is too large
   */
  @Test(expected = FactoryFailureException.class)
  public void testInvalidLenPersonCreate() throws FactoryFailureException {
    Person person = new Person("John", "Doe", 35);
    List<String> invalidPersonLen = List.of("john", "jane", "doe", "23");

    person.create(invalidPersonLen);
  }

  /**
   * Tests exception thrown when age field is invalid input
   *
   * @throws FactoryFailureException if age is invalid (not a whole numerical value)
   */
  @Test(expected = FactoryFailureException.class)
  public void testInvalidInputPersonCreate() throws FactoryFailureException {
    Person person = new Person("John", "Doe", 35);
    List<String> invalidPersonAge = List.of("John", "Doe", "Thiry-Five");

    person.create(invalidPersonAge);
  }

  /**
   * Tests valid create method for a star class.
   *
   * @throws FactoryFailureException if input row is invalid
   */
  @Test
  public void testValidStarCreate() throws FactoryFailureException {
    Star star = new Star(12, "North", 12.3, 14.5, 18);
    List<String> validStar = List.of("12", "North", "12.3", "14.5", "18");

    Assert.assertEquals(star.getProperName(), star.create(validStar).getProperName());
    Assert.assertEquals(star.getX(), star.create(validStar).getX());
  }

  /**
   * Tests exception thrown when id int input is invalid
   *
   * @throws FactoryFailureException if int value invalid
   */
  @Test(expected = FactoryFailureException.class)
  public void testInvalidIntInputStarCreate() throws FactoryFailureException {
    Star star = new Star(12, "North", 12.3, 14.5, 18);
    List<String> invalidStar = List.of("12.3", "North", "12.3", "14.5", "18");

    star.create(invalidStar);
  }

  /**
   * Tests exception thrown when double input is invalid
   *
   * @throws FactoryFailureException if double value invalid (Not number)
   */
  @Test(expected = FactoryFailureException.class)
  public void testInvalidDoubleInputStarCreate() throws FactoryFailureException {
    Star star = new Star(12, "North", 12.3, 14.5, 18);
    List<String> invalidStar = List.of("12", "North", "twelve point 3", "14.5", "18");

    star.create(invalidStar);
  }

  /**
   * Tests exception thrown when input row is too large
   *
   * @throws FactoryFailureException if list is not proper len
   */
  @Test(expected = FactoryFailureException.class)
  public void testInvalidSizeStarCreate() throws FactoryFailureException {
    Star star = new Star(12, "North", 12.3, 14.5, 18);
    List<String> invalidStar = List.of("12", "North", "12.3", "14.5", "18", "Long");

    star.create(invalidStar);
  }
}
