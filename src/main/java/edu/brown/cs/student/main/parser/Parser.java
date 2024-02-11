package edu.brown.cs.student.main.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Parser<T> {

  private BufferedReader readInput;
  private CreatorFromRow<T> strategy;
  private ArrayList<T> parsedOutput;
  static final Pattern regexSplitCSVRow =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Constructor for Parser - With Strategy Provided
   *
   * @param rawInput CSV Reader
   * @param strategy Strategy interface which decides what type rows of CSV will be converted to.
   */
  public Parser(Reader rawInput, CreatorFromRow<T> strategy) {
    this.readInput = new BufferedReader(rawInput);
    this.strategy = strategy;
  }


  /**
   * Parse method to read lines of reader and create an ArrayList of type T, corresponding with the
   * Strategy provided by the user. Catches IO exceptions if necessary as well as
   * FactoryFailureExceptions and provides messages corresponding with error encountered.
   *
   * @return List of type T
   */
  public ArrayList<T> parse() {

    this.parsedOutput = new ArrayList<T>();

    try {

      String line;

      while ((line = this.readInput.readLine()) != null) {
        String[] split = regexSplitCSVRow.split(line);
        T row = this.strategy.create(Arrays.asList(split));
        this.parsedOutput.add(row);
      }

      // Return when no errors in Rows
      //      System.out.println(this.parsedOutput);
      //      return this.parsedOutput;

    } catch (IOException e) {
      System.out.println("IO Exception encountered while Parsing");

    } catch (FactoryFailureException e) {
      System.out.println(this.parsedOutput);
      System.out.println(e.getMessage());
    }

    // Return when error is found in rows
    //    this.parsedOutput = new ArrayList<T>();
    //    System.out.println(this.parsedOutput);
    return this.parsedOutput;
  }
}
