package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.acsData.StateCodes;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private final StateCodes stateCodes;
  public BroadbandHandler(StateCodes stateCodes){
    this.stateCodes = stateCodes;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Set<String> params = request.queryParams();
    //     System.out.println(params);
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    //     System.out.println(participants);

    int stateCode = stateCodes.getCode(state);

    // Creates a hashmap to store the results of the request
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Sends a request to the API and receives JSON back
      String broadbandJson = this.sendRequest(stateCode);
      // Deserializes JSON into an Activity
      //Activity activity = ActivityAPIUtilities.deserializeActivity(activityJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      //responseMap.put("activity", activity);
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

  private String sendRequest(int parseInt) {
    return "";
  }
}
