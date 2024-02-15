package edu.brown.cs.student.TestHandlers;

import static spark.Spark.after;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import edu.brown.cs.student.main.server.ServerState;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import okio.Buffer;
import org.junit.jupiter.api.*;
import spark.Spark;

public class TestCSVHandlers {

  private static Moshi moshi;

  @BeforeAll
  public static void moshiSetup() {
    Spark.port(0);
    //    Logger.getLogger("").setLevel(Level.WARNING);
    moshi = new Moshi.Builder().build();
  }

  /**
   * Helper method to make API connections and calls when given an endpoint
   *
   * @param call API endpoint
   * @return Map of the response
   */
  public Map<String, Object> request(String call) throws IOException {

    URL requestURL = new URL("http://localhost" + Spark.port() + "/" + call);
    HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();

    connection.connect();

    Type stringObjectMap = Types.newParameterizedType(Map.class, String.class, Object.class);

    try (Buffer buffer = new Buffer().readFrom(connection.getInputStream())) {
      Map<String, Object> response =
          (Map<String, Object>) moshi.adapter(stringObjectMap).fromJson(buffer);
      connection.disconnect();
      return response;
    }
  }

  /** Setup up server. Run before all tests */
  @BeforeEach
  public void setup() {
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    ServerState state = new ServerState();
    Spark.get("searchcsv", new SearchHandler(state));
    Spark.get("loadcsv", new LoadHandler(state));
    Spark.get("viewcsv", new ViewHandler(state));

    Spark.init();
    Spark.awaitInitialization();
  }

  /** Stop endpoints and stop server */
  @AfterEach
  public void teardown() {
    Spark.unmap("searchcsv");
    Spark.unmap("loadcsv");
    Spark.unmap("viewcsv");
    Spark.awaitStop();
  }
  //
  //  @Test
  //  public void testNoFilepath() throws IOException {
  //    Map<String, Object> response = request("loadcsv?filepath=badnotapath");
  //    Assert.assertEquals("error", response.get("result"));
  //  }
}
