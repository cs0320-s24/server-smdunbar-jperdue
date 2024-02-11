package edu.brown.cs.student.main.acsData;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class StateCodes {

  private final HashMap<String, Integer> codes = new HashMap();

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

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(stateListResponse);
    System.out.println(stateListResponse.body());

    stateListResponse.body();
  }

  public int getCode(String state) {
    if (codes.containsKey(state)) {
      return codes.get(state);
    }
    throw new IllegalArgumentException("State " + state + " does not exist");
  }
}
