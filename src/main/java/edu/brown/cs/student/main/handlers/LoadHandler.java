package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.parser.Parser;
import edu.brown.cs.student.main.parser.creatorTypes.StringListStrategy;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.ServerState;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadHandler implements Route {


  private ServerState state;

  /**
   * Constructor for load handler
   * @param state current server state
   */
  public LoadHandler(ServerState state){
    this.state = state;
  }


  @Override
  public Object handle(Request request, Response response) {

    if (request.queryParams("filepath") != null){
      StringListStrategy sls = new StringListStrategy();
      try {
        FileReader reader = new FileReader(request.queryParams("filepath"));
        Parser parser = new Parser(reader, sls);
        ArrayList<List<String>> data = parser.parse();
        this.state.setCsvData(data);
        this.state.setFilepath(request.queryParams("filepath"));

        return null; // will return success response here

      } catch (FileNotFoundException e){

        return null; //return failure response here

      }
    }
      return null; // return failure response here
  }

  /**
   * Response object to send given a request
   * @param type success
   * @param message message about outcome
   */
  public record LoadSuccessResponse(String type, String message){

    /**
     * Successful response data
     * @param message message to send with success
     */
    public LoadSuccessResponse(String message){
      this("success",message);
    }

    /**
     * @return response as a json
     */
    String serialize(){

      // Instead of taking in map, build map given result and message then serialize
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

  /**
   * Response object to send given a request
   * @param type failure
   * @param message message about outcome
   */
  public record LoadFailureResponse(String type, String message){

    /**
     * Successful response data
     * @param message message to send with success
     */
    public LoadFailureResponse(String message){
      this("failure",message);
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
