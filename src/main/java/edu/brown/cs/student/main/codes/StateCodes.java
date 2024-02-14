package edu.brown.cs.student.main.codes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;

public class StateCodes extends Codes {

  private final List<List<String>> codes;
  private final HashMap<String,String> stateMap;

  public StateCodes() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildStateListRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> stateListResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildStateListRequest, HttpResponse.BodyHandlers.ofString());
    this.codes = deserializeCodes(stateListResponse.body());
    stateMap = new HashMap<>();
    for (List<String> list : this.codes) {
      stateMap.put(list.get(0),list.get(1));
    }

  }

  public String getCode(String state) {
    if (stateMap.containsKey(state)){
      return stateMap.get(state);
    } else {
      throw new IllegalArgumentException("State " + state + " does not exist");
    }
  }
}
