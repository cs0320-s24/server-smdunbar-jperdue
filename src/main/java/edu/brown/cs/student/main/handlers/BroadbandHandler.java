package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.acsData.StateCodes;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private final StateCodes stateCodes;

  public BroadbandHandler(StateCodes stateCodes) {
    this.stateCodes = stateCodes;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    System.out.println(params);
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    System.out.println(state);
    System.out.println(county);

    int stateCode = stateCodes.getCode(state);
    System.out.println(stateCode);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String broadbandJson = this.sendRequest(stateCode, county);
      // Deserializes JSON into an Activity
      StateInfo stateInfo = StateInfoUtilities.deserializeStateInfo(broadbandJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("broadband", stateInfo);
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }

  private String sendRequest(int code, String county)
      throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildApiRequest =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + county
                        + "&in=state:"
                        + code))
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
