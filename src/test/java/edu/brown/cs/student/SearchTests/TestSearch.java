package edu.brown.cs.student.SearchTests;

import edu.brown.cs.student.main.parser.Parser;
import edu.brown.cs.student.main.parser.creatorTypes.StringListStrategy;
import edu.brown.cs.student.main.search.InvalidQueryException;
import edu.brown.cs.student.main.search.Search;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSearch {

  private ArrayList<List<String>> withHeadersData;

  private ArrayList<List<String>> noHeadersData;

  private ArrayList<List<String>> empty;

  /** Setup for search testing */
  @Before
  public void setup() throws FileNotFoundException {
    StringListStrategy sls = new StringListStrategy();

    StringReader readerNoHeader =
        new StringReader("John,Gray,23\n" + "Grace,Kelly,34\n" + "Jim,Hadd,57");
    Parser noHeadersParse = new Parser(readerNoHeader, sls);
    this.noHeadersData = noHeadersParse.parse();

    FileReader readerHeader = new FileReader("data\\personal\\personWithHeaders.csv");
    Parser headersParse = new Parser(readerHeader, sls);
    this.withHeadersData = headersParse.parse();

    StringReader empty = new StringReader("");
    Parser emptyParser = new Parser<>(empty, sls);
    this.empty = emptyParser.parse();
  }

  /** Tests Empty Search will Result in empty ArrayList */
  @Test
  public void testEmptySearch() throws InvalidQueryException {

    Search emptyQ = new Search(this.empty, false, "");
    Assert.assertEquals(emptyQ.search(), new ArrayList<>());
  }

  /**
   * Tests Basic Searching in Data with No Headers and Headers
   *
   * @throws InvalidQueryException
   */
  @Test
  public void testSearchBasic() throws InvalidQueryException {
    // No Headers
    Search noHeadersQ = new Search(noHeadersData, false, "Jim");
    Assert.assertEquals(noHeadersQ.search().get(0).get(0), "Jim");
    Assert.assertEquals(noHeadersQ.search().get(0).get(1), "Hadd");
    Assert.assertEquals(noHeadersQ.search().get(0).get(2), "57");

    // Headers
    Search headersQ = new Search(withHeadersData, true, "Jim");
    Assert.assertEquals(headersQ.search().get(0).get(0), "Jim");
    Assert.assertEquals(headersQ.search().get(0).get(1), "Hadd");
    Assert.assertEquals(headersQ.search().get(0).get(2), "57");
  }

  /**
   * Tests search by index when indices are within bounds with and without header - assuming correct
   * column
   */
  @Test
  public void testSearchByIndexFunctional() throws InvalidQueryException {
    // No Headers - Correct Index
    Search noHeadersQ = new Search(noHeadersData, false, "Jim", 0);
    Assert.assertEquals(noHeadersQ.search().get(0).get(0), "Jim");
    Assert.assertEquals(noHeadersQ.search().get(0).get(1), "Hadd");
    Assert.assertEquals(noHeadersQ.search().get(0).get(2), "57");

    // Headers - Correct Index
    Search headersQ = new Search(withHeadersData, true, "Jim", 0);
    Assert.assertEquals(headersQ.search().get(0).get(0), "Jim");
    Assert.assertEquals(headersQ.search().get(0).get(1), "Hadd");
    Assert.assertEquals(headersQ.search().get(0).get(2), "57");
  }

  /** Tests search by index when the column does not match where the value is within the data */
  @Test
  public void testSearchByIndexWrongCol() throws InvalidQueryException {
    // No Headers - Incorrect Index
    Search noHeadersEmptyQ = new Search(noHeadersData, false, "Jim", 1);
    Assert.assertEquals(noHeadersEmptyQ.search(), new ArrayList<>());

    // Headers - Incorrect Index
    Search headersEmptyQ = new Search(withHeadersData, true, "Jim", 1);
    Assert.assertEquals(headersEmptyQ.search(), new ArrayList<>());
  }

  /** Test ensures invalid query exception is through is colIndex is out of bounds */
  @Test(expected = InvalidQueryException.class)
  public void testSearchByIndexOutOfBound() throws InvalidQueryException {
    // No Headers - Incorrect Index
    Search noHeadersEmptyQ = new Search(noHeadersData, false, "Jim", 3);
    Assert.assertEquals(noHeadersEmptyQ.search(), new ArrayList<>());

    // Headers - Incorrect Index
    Search headersEmptyQ = new Search(withHeadersData, true, "Jim", 3);
    Assert.assertEquals(headersEmptyQ.search(), new ArrayList<>());
  }

  /** Test basic searching when column name variable is provided */
  @Test
  public void testSearchByNameFunctional() throws InvalidQueryException {
    Search headersQ = new Search(withHeadersData, true, "Jim", "firstName");
    ArrayList<List<String>> results = headersQ.search();

    Assert.assertEquals(results.get(0).get(0), "Jim");
    Assert.assertEquals(results.get(0).get(1), "Hadd");
    Assert.assertEquals(results.get(0).get(2), "57");
  }

  /** Tests error is thrown when headers input is false */
  @Test(expected = InvalidQueryException.class)
  public void testSearchByNameHeadersFalse() throws InvalidQueryException {
    Search headersQ = new Search(withHeadersData, false, "Jim", "firstName");
    headersQ.search();
  }

  /** Tests error is thrown when input colName is not in the provided headers */
  @Test(expected = InvalidQueryException.class)
  public void testColNameNotInHeaders() throws InvalidQueryException {
    Search headersQ = new Search(withHeadersData, false, "Jim", "Gender");
    headersQ.search();
  }

  /** Tests headers will not appear in search results if query is a header name */
  @Test
  public void testHeadersDoNotAppear() throws InvalidQueryException {
    Search headersQ = new Search(withHeadersData, true, "firstName");
    ArrayList<List<String>> results = headersQ.search();

    Assert.assertEquals(results, new ArrayList<>());
  }

}
