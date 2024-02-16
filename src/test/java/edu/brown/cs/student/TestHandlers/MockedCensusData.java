package edu.brown.cs.student.TestHandlers;

import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.api_exceptions.DatasourceException;
import java.io.IOException;
import java.net.URISyntaxException;

public class MockedCensusData implements ACSDatasource {

  private final String broadband;

  public MockedCensusData(String param) {
    this.broadband = param;
  }

  @Override
  public String getBroadband(String countyCommaState)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException {
    return "[[\"name\",\"broadband\",\"state\",\"county\"],[\""
        + countyCommaState
        + "\",\""
        + this.broadband
        + "\",\"cCode\",\"sCode\"]]";
  }
}
