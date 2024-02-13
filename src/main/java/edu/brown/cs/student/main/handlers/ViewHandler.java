package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.ServerState;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class ViewHandler implements Route {

  private ServerState state;

  /**
   * Constructor for view handler
   *
   * @param state current server state
   */
  public ViewHandler(ServerState state) {
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    ArrayList<List<String>> data = this.state.getCsvData();

    if (data != null) {
      List<List<String>> results = this.state.getCsvData();
      return new ViewSuccessResponse(results).serialize();
    }
    return new ViewFailureResponse("No CSV loaded. Please Load before Viewing.").serialize();
  }

  /**
   * Response object to send given a request
   *
   * @param type success
   * @param data view results
   */
  public record ViewSuccessResponse(String type, List<List<String>> data) {

    /**
     * Successful response data
     *
     * @param data csv results from search
     */
    public ViewSuccessResponse(List<List<String>> data) {
      this("success", data);
    }

    /**
     * @return response as a json
     */
    String serialize() {

      // Instead of taking in map, build map given result and message then serialize
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("result", this.type);
      responseMap.put("data", this.data);
      Type stringObjectMap = Types.newParameterizedType(Map.class, String.class, Object.class);

      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(stringObjectMap);
        return adapter.toJson(responseMap);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /**
   * Response object to send given a request
   *
   * @param type failure
   * @param message message about outcome
   */
  public record ViewFailureResponse(String type, String message) {

    /**
     * Successful response data
     *
     * @param message message to send with success
     */
    public ViewFailureResponse(String message) {
      this("failure", "Error encountered while viewing file: " + message);
    }

    /**
     * @return response as a json
     */
    String serialize() {

      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("result", this.type);
      responseMap.put("message", this.message);
      Type stringObjectMap = Types.newParameterizedType(Map.class, String.class, Object.class);

      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(stringObjectMap);
        return adapter.toJson(responseMap);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }
}
