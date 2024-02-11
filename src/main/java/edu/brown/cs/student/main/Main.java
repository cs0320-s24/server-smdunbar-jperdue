package edu.brown.cs.student.main;

import edu.brown.cs.student.main.search.UtilitySearch;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run(args);
  }

  private Main(String[] args) {}

  private void run(String[] args) {
    // Args will be prompted as filepath, query, headers, optional identifier

    // Ensure input file is in data directory
    String path = "data/";
    String fullPath = path.concat(args[0]);

    if (args.length == 3) {
      UtilitySearch.query(fullPath, args[1], Boolean.valueOf(args[2]));
    } else if (args.length == 4) {

      try {
        int colIndex = Integer.parseInt(args[3]);
        UtilitySearch.query(fullPath, args[1], Boolean.valueOf(args[2]), colIndex);
      } catch (NumberFormatException e) {
        UtilitySearch.query(fullPath, args[1], Boolean.valueOf(args[2]), args[3]);
      }
    } else {
      System.out.println(
          "Incorrect amount of arguments provided. Please follow the following format: \n"
              + "filepath, value you are searching for, headers (boolean true or false), (optional) column index or name");
    }
  }
}
