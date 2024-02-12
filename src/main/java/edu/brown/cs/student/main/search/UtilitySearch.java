package edu.brown.cs.student.main.search;

import edu.brown.cs.student.main.parser.Parser;
import edu.brown.cs.student.main.parser.creatorTypes.StringListStrategy;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UtilitySearch {

  /**
   * Utility method to Query a file for rows within a CSV that contain an input query value
   *
   * @param data parsed CSV data
   * @param query String value to search for within CSV
   * @param headers whether the input CSV file has headers
   */
  public static List<List<String>> query(ArrayList<List<String>> data, String query, Boolean headers) throws FileNotFoundException, InvalidQueryException {

    Search search = new Search(data, headers, query);
    return search.search();


//      printResults(search.search());

  }

  /**
   * Utility method to Query a file for rows within a CSV that contain an input query value
   *
   * @param data parsed CSV data
   * @param query String value to search for within CSV
   * @param headers whether the input CSV file has headers
   * @param colIndex index value of the column to search
   */
  public static List<List<String>> query(ArrayList<List<String>> data, String query, Boolean headers, int colIndex) throws InvalidQueryException, FileNotFoundException {



    Search search = new Search(data, headers, query, colIndex);
    return search.search();

//      printResults(search.search());

  }

  /**
   * Utility method to Query a file for rows within a CSV that contain an input query value
   *
   * @param data parsed CSV data
   * @param query String value to search for within CSV
   * @param headers whether the input CSV file has headers
   * @param colName string name of the column to search
   */
  public static List<List<String>> query(ArrayList<List<String>> data, String query, Boolean headers, String colName) throws FileNotFoundException, InvalidQueryException {

    Search search = new Search(data, headers, query, colName);
    return search.search();

    //      printResults(search.search());

  }

//  /**
//   * Private helper method to print resulting rows from a query
//   *
//   * @param results results from search method.
//   */
//  private static void printResults(ArrayList<List<String>> results) {
//    if (results.isEmpty()) {
//      System.out.println("The input search yielded no matching rows. Please try a new search.");
//    } else {
//      System.out.println("Search Results:");
//      for (List<String> row : results) {
//        System.out.println(row);
//      }
//    }
//  }
}
