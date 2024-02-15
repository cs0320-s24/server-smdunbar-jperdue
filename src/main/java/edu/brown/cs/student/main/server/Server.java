package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.acsData.CachingCensusData;
import edu.brown.cs.student.main.acsData.CensusAPI;
import edu.brown.cs.student.main.codes.StateCodes;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers (2). credit: gear up code
 */
public class Server {
  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException {
    int port = 3232;
    Spark.port(port);
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Sets up data needed for the OrderHandler. You will likely not read from local
    //    // JSON in this sprint.
    //    String menuAsJson = SoupAPIUtilities.readInJson("data/menu.json");
    //    List<Soup> menu = new ArrayList<>();
    //    try {
    //      menu = SoupAPIUtilities.deserializeMenu(menuAsJson);
    //    } catch (Exception e) {
    //      // See note in ActivityHandler about this broad Exception catch... Unsatisfactory, but
    // gets
    //      // the job done in the gearup where it is not the focus.
    //      e.printStackTrace();
    //      System.err.println("Errored while deserializing the menu");
    //    }

    StateCodes stateMap = new StateCodes();
    // deal with this not being defensive to pass in later^^ editable?
    CachingCensusData cachedCensusAPI = new CachingCensusData(new CensusAPI(stateMap), 10, 1);

    ServerState state = new ServerState();
    // Setting up the handler for the GET /order and /activity endpoints
    Spark.get("searchcsv", new SearchHandler(state));
    Spark.get("loadcsv", new LoadHandler(state));
    Spark.get("viewcsv", new ViewHandler(state));
    Spark.get("broadband", new BroadbandHandler(cachedCensusAPI));

    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started at http://localhost:" + port);
  }
}
