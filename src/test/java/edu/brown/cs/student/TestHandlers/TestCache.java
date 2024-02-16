package edu.brown.cs.student.TestHandlers;

import static spark.Spark.after;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.acsData.CachingCensusData;
import edu.brown.cs.student.main.acsData.CensusAPI;
import edu.brown.cs.student.main.codes.StateCodes;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.server.ServerState;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestCache {

  private final JsonAdapter<Map<String, Object>> adapter;
  StateCodes stateMap = new StateCodes();
  CachingCensusData cachedCensusAPI = new CachingCensusData(new CensusAPI(stateMap), 5, 1);

  public TestCache() throws URISyntaxException, IOException, InterruptedException {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    adapter = moshi.adapter(type);
  }

  @BeforeClass
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });
    // Re-initialize state, etc. for _every_ test method run
    ServerState state = new ServerState();
    ACSDatasource mockedSource = new MockedCensusData("93.1");
    // In fact, restart the entire Spark server for every test!
    Spark.get("broadband", new BroadbandHandler(cachedCensusAPI));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("loadcsv");
    Spark.unmap("searchcsv");
    Spark.unmap("viewcsv");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  /** Tests that the cache stores and recalls items */
  @Test
  public void testCache1() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Assert.assertEquals(1, this.cachedCensusAPI.getCacheSize());

    HttpURLConnection clientConnection2 =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection2.getResponseCode());
    Assert.assertEquals(1, this.cachedCensusAPI.getCacheSize());
    HttpURLConnection clientConnection3 =
        tryRequest("broadband?state=California&county=Kings%20County");
    Assert.assertEquals(200, clientConnection3.getResponseCode());
    Assert.assertEquals(2, this.cachedCensusAPI.getCacheSize());
    HttpURLConnection clientConnection4 =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection4.getResponseCode());
    Assert.assertEquals(2, this.cachedCensusAPI.getCacheSize());
    clientConnection.disconnect();
  }
  /** tests that the cache removes items after 5 items are reached */
  @Test
  public void testCacheMax() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Assert.assertEquals(1, this.cachedCensusAPI.getCacheSize());

    HttpURLConnection clientConnection2 =
        tryRequest("broadband?state=California&county=Napa%20County");
    Assert.assertEquals(200, clientConnection2.getResponseCode());
    Assert.assertEquals(2, this.cachedCensusAPI.getCacheSize());
    HttpURLConnection clientConnection3 =
        tryRequest("broadband?state=California&county=Kings%20County");
    Assert.assertEquals(200, clientConnection3.getResponseCode());
    Assert.assertEquals(3, this.cachedCensusAPI.getCacheSize());
    HttpURLConnection clientConnection4 =
        tryRequest("broadband?state=California&county=Santa%20Barbara%20County");
    Assert.assertEquals(200, clientConnection4.getResponseCode());
    Assert.assertEquals(4, this.cachedCensusAPI.getCacheSize());
    HttpURLConnection clientConnection5 =
        tryRequest("broadband?state=California&county=San%20Diego%20County");
    Assert.assertEquals(200, clientConnection5.getResponseCode());
    Assert.assertEquals(5, this.cachedCensusAPI.getCacheSize());
    HttpURLConnection clientConnection6 =
        tryRequest("broadband?state=California&county=Nevada%20County");
    Assert.assertEquals(200, clientConnection6.getResponseCode());
    Assert.assertEquals(5, this.cachedCensusAPI.getCacheSize());
    clientConnection.disconnect();
  }
  /** tests that the cache removes items after a minute */
  @Test
  public void testCacheTime() throws IOException, InterruptedException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Assert.assertEquals(1, this.cachedCensusAPI.getCacheSize());
    Thread.sleep(60000);
    HttpURLConnection clientConnection2 =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection2.getResponseCode());
    Assert.assertEquals(1, this.cachedCensusAPI.getCacheSize());
    clientConnection.disconnect();
  }
}
