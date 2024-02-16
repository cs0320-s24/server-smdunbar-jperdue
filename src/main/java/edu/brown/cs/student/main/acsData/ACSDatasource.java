package edu.brown.cs.student.main.acsData;

import edu.brown.cs.student.main.api_exceptions.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface ACSDatasource {
  /** Interface for all datasources to be passed into BroadbandHandler */

  /**
   * @param countyCommaState is the county a comma then the stt=ate provided
   * @return
   * @throws URISyntaxException when uri is malformed
   * @throws IOException with incorrect user input
   * @throws InterruptedException
   * @throws DatasourceException if the datasource provides an error
   */
  public String getBroadband(String countyCommaState)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException;
}
