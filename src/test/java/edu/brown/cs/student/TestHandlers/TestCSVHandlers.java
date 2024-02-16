package edu.brown.cs.student.TestHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static spark.Spark.after;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import edu.brown.cs.student.main.server.ServerState;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestCSVHandlers {

  private final JsonAdapter<Map<String, Object>> adapter;

  public TestCSVHandlers() {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    adapter = moshi.adapter(type);
  }

  @BeforeAll
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
    Spark.get("loadcsv", new LoadHandler(state));
    Spark.get("searchcsv", new SearchHandler(state));
    Spark.get("viewcsv", new ViewHandler(state));
    Spark.get("broadband", new BroadbandHandler(mockedSource));

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

  /** Tests when a csv is successfully loaded to the state */
  @Test
  public void testLoadSuccess() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("loadcsv?filepath=data/personal/personWithHeaders.csv&headers=true");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests loadcsv fails due to invalid path */
  @Test
  public void testLoadFailureInvalidPath() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("loadcsv?filepath=data/person/notAFile&headers=true");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when loadcsv fails due to no path provided */
  @Test
  public void testLoadFailureNoPath() throws IOException {
    HttpURLConnection clientConnection = tryRequest("loadcsv");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when viewcsv is successful */
  @Test
  public void testViewSuccess() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithHeaders.csv&headers=true");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("viewcsv");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when viewcsv fails as no csv is loaded */
  @Test
  public void testViewFailure() throws IOException {
    HttpURLConnection clientConnection = tryRequest("viewcsv");
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv is successful */
  @Test
  public void testSearchSuccessNoColumnID() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithHeaders.csv&headers=true");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv is successful */
  @Test
  public void testSearchSuccessColumnName() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithHeaders.csv&headers=true");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim&column=firstName");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv is successful */
  @Test
  public void testSearchSuccessColumnIndex() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithHeaders.csv&headers=true");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim&column=0");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv is successful */
  @Test
  public void testSearchSuccessNoResult() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithHeaders.csv&headers=true");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=James");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv fails because no CSV loaded */
  @Test
  public void testSearchFailueNoLoad() throws IOException {
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv fails searching by header with no headers */
  @Test
  public void testSearchFailureNoHeaders() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithoutHeaders.csv&headers=false");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim&column=firstName");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv fails searching by header and the column doesn't exist */
  @Test
  public void testSearchFailureHeadersNoExist() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithoutHeaders.csv&headers=false");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim&column=first_Name");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv fails searching by header and the index is out of bounds */
  @Test
  public void testSearchFailureIndexOutOfBounds() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithoutHeaders.csv&headers=false");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?query=Jim&column=5");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }

  /** Tests when searchcsv fails no query provided */
  @Test
  public void testSearchFailureNoQuery() throws IOException {
    HttpURLConnection clientConnectionLoad =
        tryRequest("loadcsv?filepath=data/personal/personWithoutHeaders.csv&headers=false");
    Assert.assertEquals(200, clientConnectionLoad.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }
}
