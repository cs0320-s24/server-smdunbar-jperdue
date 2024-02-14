package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.acsData.CensusAPI;
import edu.brown.cs.student.main.acsData.StateCodes;
import edu.brown.cs.student.main.acsData.StateInfoUtilities;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private final StateCodes stateCodes;
  private CensusAPI censusAPI;

  public BroadbandHandler(CensusAPI census, StateCodes stateCodes) {
    this.stateCodes = stateCodes;
    this.censusAPI = census;
  }

  @Override
  public Object handle(Request request, Response response) {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    String stateCode = stateCodes.getCode(state);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String broadbandJson = censusAPI.sendRequest(stateCode, county);
      // Deserializes JSON into an Activity
      List<List> stateInfo = StateInfoUtilities.deserializeStateInfo(broadbandJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("broadband", stateInfo.get(1).get(1));
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
}
