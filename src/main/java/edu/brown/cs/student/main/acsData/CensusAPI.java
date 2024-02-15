package edu.brown.cs.student.main.acsData;

import edu.brown.cs.student.main.codes.CountyCodes;
import edu.brown.cs.student.main.codes.StateCodes;
import edu.brown.cs.student.main.api_exceptions.DatasourceException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CensusAPI implements ACSDatasource {

  private StateCodes stateCodes;

  public CensusAPI(StateCodes sc) {
    this.stateCodes = sc;
  }

  @Override
  public String getBroadband(String countyCommaState)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException {
    String[] strArr = countyCommaState.split(",");
    String county = strArr[0];
    String state = strArr[1];
    System.out.println(county);
    System.out.println(state);
    CountyCodes countyCodes = new CountyCodes(stateCodes, state);
    String countyCode;
    String stateCode;
    try {
      countyCode = countyCodes.getCountyCode(county);
      stateCode = stateCodes.getCode(state);
    } catch (IllegalArgumentException e) {
      throw new DatasourceException("Datasource could not complete request: " + e.getMessage());
    }
    System.out.println(countyCode);
    System.out.println(stateCode);
    HttpRequest buildApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + countyCode
                        + "&in=state:"
                        + stateCode))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentApiResponse =
        HttpClient.newBuilder().build().send(buildApiRequest, HttpResponse.BodyHandlers.ofString());

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(sentApiResponse);
    System.out.println(sentApiResponse.body());
    return sentApiResponse.body();
  }
}
