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
   * @param filePath String path for a file
   * @param query String value to search for within CSV
   * @param headers whether the input CSV file has headers
   */
  public static void query(String filePath, String query, Boolean headers) {

    StringListStrategy sls = new StringListStrategy();

    try {
      FileReader reader = new FileReader(filePath);
      Parser parser = new Parser(reader, sls);
      Search search = new Search(parser.parse(), headers, query);

      printResults(search.search());

    } catch (FileNotFoundException e) {
      System.out.println(
          "File: " + filePath + " was not found.\n" + "Try again using a different file path.");
    } catch (InvalidQueryException e) {
      System.out.println("Input query encountered the following error: ");
      System.out.println(e.getMessage());
    }
  }

  /**
   * Utility method to Query a file for rows within a CSV that contain an input query value
   *
   * @param filePath String path for a file
   * @param query String value to search for within CSV
   * @param headers whether the input CSV file has headers
   * @param colIndex index value of the column to search
   */
  public static void query(String filePath, String query, Boolean headers, int colIndex) {

    StringListStrategy sls = new StringListStrategy();

    try {
      FileReader reader = new FileReader(filePath);
      Parser parser = new Parser(reader, sls);
      Search search = new Search(parser.parse(), headers, query, colIndex);

      printResults(search.search());

    } catch (FileNotFoundException e) {
      System.out.println(
          "File: " + filePath + "was not found. Try again using a different file path.");
    } catch (InvalidQueryException e) {
      System.out.println("Input query encountered the following error: ");
      System.out.println(e.getMessage());
    }
  }

  /**
   * Utility method to Query a file for rows within a CSV that contain an input query value
   *
   * @param filePath String path for a file
   * @param query String value to search for within CSV
   * @param headers whether the input CSV file has headers
   * @param colName string name of the column to search
   */
  public static void query(String filePath, String query, Boolean headers, String colName) {

    StringListStrategy sls = new StringListStrategy();

    try {
      FileReader reader = new FileReader(filePath);
      Parser parser = new Parser(reader, sls);
      Search search = new Search(parser.parse(), headers, query, colName);

      printResults(search.search());

    } catch (FileNotFoundException e) {
      System.out.println(
          "File: " + filePath + "was not found. Try again using a different file path.\n" +
              "Additionally, make sure your desired file is in the data directory.");
    } catch (InvalidQueryException e) {
      System.out.println("Input query encountered the following error: ");
      System.out.println(e.getMessage());
    }
  }

  /**
   * Private helper method to print resulting rows from a query
   *
   * @param results results from search method.
   */
  private static void printResults(ArrayList<List<String>> results) {
    if (results.isEmpty()) {
      System.out.println("The input search yielded no matching rows. Please try a new search.");
    } else {
      System.out.println("Search Results:");
      for (List<String> row : results) {
        System.out.println(row);
      }
    }
  }
}
