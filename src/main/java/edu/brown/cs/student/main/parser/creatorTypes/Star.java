package edu.brown.cs.student.main.parser.creatorTypes;

import edu.brown.cs.student.main.parser.CreatorFromRow;
import edu.brown.cs.student.main.parser.FactoryFailureException;
import java.util.List;

public class Star implements CreatorFromRow<Star> {

  // Data to conform with Stars CSV file - testing purposes
  private int starId;
  private String properName;
  private double x;
  private double y;
  private double z;

  /**
   * Creates a new Star
   *
   * @param starId int ID number in data
   * @param properName String name of star
   * @param x double x coordinate
   * @param y double y coordinate
   * @param z double z coordinate
   */
  public Star(int starId, String properName, double x, double y, double z) {
    this.starId = starId;
    this.properName = properName;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override
  public Star create(List<String> row) throws FactoryFailureException {
    if (row.size() == 5) {
      try {
        starId = Integer.parseInt(row.get(0));
        properName = row.get(1);
        x = Double.parseDouble(row.get(2));
        y = Double.parseDouble(row.get(3));
        z = Double.parseDouble(row.get(4));
        return new Star(starId, properName, x, y, z);
      } catch (NumberFormatException e) {
        throw new FactoryFailureException("ID or Coordinates Formatted Incorrectly", row);
      }
    } else {
      throw new FactoryFailureException("Incorrect Row Size", row);
    }
  }

  /**
   * Getter for testing purposes
   *
   * @return x double value
   */
  public double getX() {
    return x;
  }

  /**
   * Getter for testing purposes
   *
   * @return String name value
   */
  public String getProperName() {
    return properName;
  }
}
