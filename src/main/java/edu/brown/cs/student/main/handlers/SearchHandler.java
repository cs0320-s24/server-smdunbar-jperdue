package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.search.InvalidQueryException;
import edu.brown.cs.student.main.search.UtilitySearch;
import edu.brown.cs.student.main.server.ServerState;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchHandler implements Route {

  private ServerState state;

  /**
   * Constructor for load handler
   * @param state current server state
   */
  public SearchHandler(ServerState state){
    this.state = state;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    ArrayList<List<String>> data = this.state.getCsvData();
    boolean headers = this.state.getHeaders();

    if (data != null){
      String query = request.queryParams("query");
      String column = request.queryParams("column");
      if (query != null){
        if (column != null){
          try { // when column identifier is provided
            List<List<String>> results = UtilitySearch.query(data, query, headers, Integer.parseInt(column));
            return new SearchSuccessResponse(results);


          } catch (NumberFormatException e) {
            try { // when column identifier is not an integer but rather a name
              List<List<String>> results = UtilitySearch.query(data, query, headers);
              return new SearchSuccessResponse(results);
            } catch (InvalidQueryException iqe) {
              return new SearchFailureResponse(iqe.getMessage());
            }


          } catch (InvalidQueryException e) {
            return new SearchFailureResponse(e.getMessage());
          }



        } else { // when no column identifier is provided
          try {
            List<List<String>> results = UtilitySearch.query(data, query, headers);
            return new SearchSuccessResponse(results);
          } catch (InvalidQueryException e) {
            return new SearchFailureResponse(e.getMessage());
          }
        }




      } else {
        return new SearchFailureResponse("No query provided. Provide a query to search your data.");
      }
    } else {
      return new SearchFailureResponse("No CSV loaded. Please Load before Searching.");
    }
  }



  /**
   * Response object to send given a request
   * @param type success
   * @param data search results
   */
  public record SearchSuccessResponse(String type, List<List<String>> data){

    /**
     * Successful response data
     * @param data csv results from search
     */
    public SearchSuccessResponse(List<List<String>> data){
      this("success", data);
    }

    /**
     * @return response as a json
     */
    String serialize(){

      // Instead of taking in map, build map given result and message then serialize
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("result", this.type);
      responseMap.put("message", this.data);
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
   * @param type failure
   * @param message message about outcome
   */
  public record SearchFailureResponse(String type, String message){

    /**
     * Successful response data
     * @param message message to send with success
     */
    public SearchFailureResponse(String message){
      this("failure", "Error encountered while searching file: " + message);
    }

    /**
     * @return response as a json
     */
    String serialize(){

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
