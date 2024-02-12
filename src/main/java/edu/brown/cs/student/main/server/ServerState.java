package edu.brown.cs.student.main.server;

import java.util.ArrayList;
import java.util.List;

public class ServerState {

  private ArrayList<List<String>> csvData;
  private String filepath;

  private boolean headers;

  /** Constructor for state does nothing. Will set values in their own methods */
  public ServerState() {}

  /**
   * Takes an input filepath and sets it to the filepath field of the state
   *
   * @param filepath String filepath
   */
  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }

  /**
   * Takes input parsed csvData and assigns it to the state
   *
   * @param csvData list<list<string>> parsed data
   */
  public void setCsvData(ArrayList<List<String>> csvData) {
    this.csvData = csvData;
  }

  /**
   * Takes input and sets headers as provided value
   *
   * @param headers boolean true means the data has headers
   */
  public void setHeaders(boolean headers) {
    this.headers = headers;
  }

  /**
   * Getter for filepath string
   *
   * @return string filepath
   */
  public String getFilepath() {
    return this.filepath;
  }

  /**
   * Returns csv data
   *
   * @return csv data field
   */
  public ArrayList<List<String>> getCsvData() {
    return this.csvData;
  }

  /**
   * Returns boolean headers
   *
   * @return boolean headers
   */
  public boolean getHeaders() {
    return this.headers;
  }

  /** Clears state data and sets both values to null. */
  public void clearData() {
    this.filepath = null;
    this.csvData = null;
    this.headers = false;
  }
}
