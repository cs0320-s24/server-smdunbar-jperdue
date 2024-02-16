package edu.brown.cs.student.TestHandlers;

import edu.brown.cs.student.main.acsData.ACSDatasource;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestSerialize {

  public BroadbandHandler handler;

  @BeforeEach
  public void setup() {
    ACSDatasource mockedSource = new MockedCensusData("93.1");
    this.handler = new BroadbandHandler(mockedSource);
  }

  @Test
  public void testSerialize() {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put("result", "success");
    responseMap.put("state", "California");
    responseMap.put("county", "Orange County");
    responseMap.put("broadband", "93.1");
    String reply = this.handler.serialize(responseMap);
    String expectedReply =
        "{\"result\":\"success\",\"broadband\":\"93.1\",\"county\":\"Orange County\",\"state\":\"California\"}";

    Assert.assertTrue(reply.equals(expectedReply));
  }
}
