package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.acsData.StateInfoUtilities;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private ACSDatasource censusAPI;

  /**
   * @param census datasource to use
   */
  public BroadbandHandler(ACSDatasource census) {
    this.censusAPI = census;
  }

  @Override
  public Object handle(Request request, Response response)
      throws URISyntaxException, IOException, InterruptedException {
    String state = request.queryParams("state");
    String county = request.queryParams("county");
    Map<String, Object> responseMap = new HashMap<>();
    try {
      if (state == null || county == null) {
        throw new IOException("error_bad_request");
      }

      // Creates a hashmap to store the results of the request

      // Sends a request to the API and receives JSON back
      String broadbandJson = censusAPI.getBroadband(county + "," + state);
      // Deserializes JSON into an Activity
      List<List> stateInfo = StateInfoUtilities.deserializeStateInfo(broadbandJson);
      // Adds results to the responseMap
      responseMap.put("result", "success");
      responseMap.put("state", state);
      responseMap.put("county", county);
      responseMap.put("broadband", stateInfo.get(1).get(1));
      return serialize(responseMap);
    } catch (Exception e) {
      responseMap.put("result", "failure");
      responseMap.put("message", e.getMessage());
    }

    return serialize(responseMap);
  }

  /**
   * serializes reponse map into json
   *
   * @param map map to be serialized
   * @return String version of the map
   */
  private String serialize(Map<String, Object> map) {
    Type stringObjectMap = Types.newParameterizedType(Map.class, String.class, Object.class);
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(stringObjectMap);
    return adapter.toJson(map);
  }
}
