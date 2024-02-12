package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.parser.Parser;
import edu.brown.cs.student.main.parser.creatorTypes.StringListStrategy;
import edu.brown.cs.student.main.server.Server;
import edu.brown.cs.student.main.server.ServerState;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
   * @param success success
   * @param message message about outcome
   */
  public record LoadSuccessResponse(String success, String message){

    /**
     * Successful response data
     * @param message message to send with success
     */
    public LoadSuccessResponse(String message){
      this("success",message);
    }

    String serialize(){
      return null;

    }






  }


}
