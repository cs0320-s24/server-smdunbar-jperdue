package edu.brown.cs.student.main.search;

import java.util.ArrayList;
import java.util.List;

public class Search {

  private ArrayList<List<String>> fullData;
  private Boolean headers;
  private String query;
  private String colName;
  private Integer colIndex;

  /** Constructor for Search without index or col name provided Assign inputs to fields */
  public Search(ArrayList<List<String>> fullData, Boolean headers, String query) {
    this.fullData = fullData;
    this.headers = headers;
    this.query = query;
  }

  /** Constructor for Search with col name provided Assign inputs to fields */
  public Search(ArrayList<List<String>> fullData, Boolean headers, String query, String colName) {
    this.fullData = fullData;
    this.headers = headers;
    this.query = query;
    this.colName = colName;
  }

  /** Constructor for Search with col name provided Assign inputs to fields */
  public Search(ArrayList<List<String>> fullData, Boolean headers, String query, int colIndex) {
    this.fullData = fullData;
    this.headers = headers;
    this.query = query;
    this.colIndex = colIndex;
  }

  /**
   * Search method to locate rows with the users query Can use column identifiers or search entire
   * CSV for query
   *
   * @return a list of lists of strings that contain the provided query
   */
  public ArrayList<List<String>> search() throws InvalidQueryException {

    ArrayList<List<String>> output = new ArrayList<List<String>>();

    if (headers) { // If headers provided remove header line and search based on provided
      // identifiers
      List<String> colNames = this.fullData.get(0);

      if (this.colName != null) {
        if (!colNames.contains(this.colName)) {
          throw new InvalidQueryException(
              "Column Name provided for search is not one of the provided Data's column headers");
        }
        int index = colNames.indexOf(this.colName);
        output = this.searchWithColIndex(index);
      } else if (this.colIndex != null) {
        output = this.searchWithColIndex(this.colIndex);
      } else {
        output = this.searchWithoutIndex();
      }

    } else { // No headers, throw error if searching by colName and search normally for other two
      // options

      if (this.colName != null) {
        throw new InvalidQueryException(
            "Column Headers not Provided so cannot Search By Column Name");
      } else if (this.colIndex != null) {
        output = this.searchWithColIndex(this.colIndex);
      } else {
        output = this.searchWithoutIndex();
      }
    }

    //    System.out.println(output);
    return output;
  }

  /**
   * Helper method for search - Usage when no col index is provided
   *
   * @return A list of lists of strings which contain the query provided
   */
  private ArrayList<List<String>> searchWithoutIndex() {

    ArrayList<List<String>> output = new ArrayList<List<String>>();

    for (List<String> row : this.fullData) {
      if (row.contains(this.query)) {
        output.add(row);
      }
    }

    return output;
  }

  /**
   * Helper method for search - Usage when an index is provided
   *
   * @return A list of lists of strings which contain the query provided
   */
  private ArrayList<List<String>> searchWithColIndex(int index) throws InvalidQueryException {

    ArrayList<List<String>> output = new ArrayList<List<String>>();

    for (List<String> row : this.fullData) {
      if (index > row.size() - 1) {
        throw new InvalidQueryException("Column Index out of Bounds of Provided CSV");
      } else if (row.get(index).equals(query)) {
        output.add(row);
      }
    }

    return output;
  }
}
