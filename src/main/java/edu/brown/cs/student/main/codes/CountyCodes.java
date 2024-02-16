package edu.brown.cs.student.main.codes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

public class CountyCodes extends Codes {
  private final List<List<String>> countyCodes;
  private final StateCodes stateCodes;

  private final String state;
  private final HashMap<String, String> countyMap;

  /** queries the datasource once to get and deserialize the county codes json for the given state
   *
   * @param sc StateCodes object to loook through
   * @param state state that county is in
   * @throws IOException
   * @throws InterruptedException
   * @throws URISyntaxException
   */
  public CountyCodes(StateCodes sc, String state)
      throws IOException, InterruptedException, URISyntaxException {
    this.stateCodes = sc;
    this.state = state;

    HttpRequest buildStateListRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
                        + stateCodes.getCode(state)))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> stateListResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildStateListRequest, HttpResponse.BodyHandlers.ofString());
    this.countyCodes = deserializeCodes(stateListResponse.body());
    countyMap = new HashMap<>();
    for (List<String> list : this.countyCodes) {
      countyMap.put(list.get(0).split(",")[0], list.get(2));
    }
  }

  /**
   * gets the county code of the given county
   * @param county county to find code of
   * @return string of the code
   */
  public String getCountyCode(String county) {
    if (countyMap.containsKey(county)) {
      return countyMap.get(county);
    } else {
      throw new IllegalArgumentException(county + " does not exist in given state " + state);
    }
  }
}
