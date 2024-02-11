package edu.brown.cs.student;

import edu.brown.cs.student.main.parser.FactoryFailureException;
import edu.brown.cs.student.main.parser.creatorTypes.Person;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class ParserTest {

  //  @Test(expected = FactoryFailureException.class)
  //  public void testInvalidLenPersonCreate() throws FactoryFailureException {
  //    Person person = new Person("blank", "blank", 1);
  //    List<String> validPerson = List.of("John", "Doe", "35");
  //    List<String> invalidPersonLen = List.of("john", "jane", "doe", "23");
  //    List<String> invalidPersonAge = List.of("John", "Doe", "Thiry-Five");
  //
  //    person.create(invalidPersonLen);
  //  }

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
}
