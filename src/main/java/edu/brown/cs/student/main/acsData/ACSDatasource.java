package edu.brown.cs.student.main.acsData;

import edu.brown.cs.student.main.acsData.StateCodes;
import java.io.IOException;
import java.net.URISyntaxException;

public interface ACSDatasource {

  public String sendRequest(String code, String county)
      throws URISyntaxException, IOException, InterruptedException;


}
