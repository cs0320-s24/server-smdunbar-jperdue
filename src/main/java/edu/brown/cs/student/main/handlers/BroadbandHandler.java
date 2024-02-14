package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.acsData.StateInfoUtilities;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private ACSDatasource censusAPI;

  public BroadbandHandler(ACSDatasource census) {
    this.censusAPI = census;
  }

  @Override
  public Object handle(Request request, Response response) {
    String state = request.queryParams("state");
    String county = request.queryParams("county");

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String broadbandJson = censusAPI.getBroadband(county + "," + state);
      // Deserializes JSON into an Activity
      List<List> stateInfo = StateInfoUtilities.deserializeStateInfo(broadbandJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("broadband", stateInfo.get(1).get(1));
      return responseMap;
    } catch (Exception e) { // catch better exceptions
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }
}
