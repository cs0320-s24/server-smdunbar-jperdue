package edu.brown.cs.student.main.acsData;

import edu.brown.cs.student.main.acsData.StateCodes;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

public interface ACSDatasource {


  public String getBroadband(String countyCommaState)
      throws URISyntaxException, IOException, InterruptedException;
}
