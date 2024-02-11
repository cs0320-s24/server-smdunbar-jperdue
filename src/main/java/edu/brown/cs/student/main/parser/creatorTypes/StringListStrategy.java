package edu.brown.cs.student.main.parser.creatorTypes;

import edu.brown.cs.student.main.parser.CreatorFromRow;
import java.util.List;

public class StringListStrategy implements CreatorFromRow<List<String>> {

  @Override
  public List<String> create(List<String> row) {
    return row;
  }
}
