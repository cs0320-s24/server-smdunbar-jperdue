package edu.brown.cs.student.TestHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
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

public class TestMock {

  private final JsonAdapter<Map<String, Object>> adapter;

  public TestMock() {
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
    ACSDatasource mockedSource = new MockedCensusData("93.1");
    Spark.get("broadband", new BroadbandHandler(mockedSource));
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  /** Tests with mock that requsst work */
  @Test
  public void testBroadbandSuccess() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Orange%20County");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    assertEquals("93.1", response.get("broadband"));

    clientConnection.disconnect();
  }
  /** Tests with mock that requsst work */
  @Test
  public void testBroadbandSuccess2() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Yellow%20County");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    assertEquals("93.1", response.get("broadband"));

    clientConnection.disconnect();
  }
  /** Tests with mock that requsst work */
  @Test
  public void testBroadbandSuccess3() throws IOException {
    HttpURLConnection clientConnection =
        tryRequest("broadband?state=California&county=Purple%20County");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("success", response.get("result"));
    assertEquals("93.1", response.get("broadband"));

    clientConnection.disconnect();
  }
  /** Tests with mock bad request */
  @Test
  public void testBroadbandBadRequest() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadband");
    Assert.assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    Assert.assertEquals("failure", response.get("result"));
    clientConnection.disconnect();
  }
}
