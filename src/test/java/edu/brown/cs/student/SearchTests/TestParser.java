package edu.brown.cs.student.SearchTests;

import edu.brown.cs.student.main.parser.Parser;
import edu.brown.cs.student.main.parser.creatorTypes.Person;
import edu.brown.cs.student.main.parser.creatorTypes.Star;
import edu.brown.cs.student.main.parser.creatorTypes.StringListStrategy;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestParser {

  private Parser<Person> withStrategy;
  private Parser<Person> stringReaderParse;
  private Parser<Person> empty;

  private Parser<Star> largeParse;

  private Parser<List<String>> stringParse;

  /**
   * Sets up Parsers for Testing - Checks for no error in Constructor
   *
   * @throws FileNotFoundException if File path incorrect
   */
  @Before
  public void setup() throws FileNotFoundException {
    FileReader readerNoHeader = new FileReader("data\\personal\\personWithoutHeaders.csv");
    Person person = new Person("John", "Doe", 35);
    this.withStrategy = new Parser(readerNoHeader, person);

    StringReader stringReader =
        new StringReader("John,Gray,23\n" + "Grace,Kelly,34\n" + "Jim,Hadd,56");
    this.stringReaderParse = new Parser<>(stringReader, person);

    StringReader error = new StringReader("");
    this.empty = new Parser<>(error, person);

    Star star = new Star(12, "north", 12.3, 13.4, 56.5);
    FileReader starsFile = new FileReader("data\\stars\\starDataNoHeader.csv");
    this.largeParse = new Parser<>(starsFile, star);

    StringListStrategy sls = new StringListStrategy();
    this.stringParse = new Parser<>(stringReader, sls);
  }

  /** Tests parser with a strategy provided */
  @Test
  public void testParseWithStrategy() {
    ArrayList<Person> parsed = this.withStrategy.parse();

    Assert.assertEquals(parsed.size(), 3);
    Assert.assertEquals(parsed.get(0).getFirstName(), "John");
    Assert.assertEquals(parsed.get(0).getAge(), 23);
  }

  /** Tests parser when provided a StringReader as opposed to FileReader */
  @Test
  public void testParseStringReader() {
    ArrayList<Person> parsed = this.stringReaderParse.parse();

    Assert.assertEquals(parsed.size(), 3);
    Assert.assertEquals(parsed.get(1).getFirstName(), "Grace");
    Assert.assertEquals(parsed.get(2).getAge(), 56);
  }

  /** Tests parser with an empty file */
  @Test
  public void testEmpty() {
    ArrayList<Person> empty = this.empty.parse();

    Assert.assertEquals(empty.size(), 0);
  }

  /** Tests a parse with a large data set */
  @Test
  public void testLargeParse() {
    ArrayList<Star> stars = this.largeParse.parse();
    Assert.assertEquals(stars.size(), 119617);
  }

  /** Tests a parse using the String strategy to provide lists of strings as data in the parse */
  @Test
  public void testStringParse() {
    ArrayList<List<String>> strings = this.stringParse.parse();

    Assert.assertEquals(strings.size(), 3);
    Assert.assertEquals(strings.get(0).size(), 3);
    Assert.assertEquals(strings.get(0).get(0), "John");
  }
}
