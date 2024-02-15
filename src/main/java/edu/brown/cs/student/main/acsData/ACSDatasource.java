package edu.brown.cs.student.main.acsData;

import edu.brown.cs.student.main.api_exceptions.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface ACSDatasource {

  public String getBroadband(String countyCommaState)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException;
}
