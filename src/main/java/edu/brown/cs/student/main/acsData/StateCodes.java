package edu.brown.cs.student.main.acsData;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StateCodes {

  private final List<List<String>> codes;
  private final HashMap<String,String> stateMap;

  public StateCodes() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest buildStateListRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> stateListResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildStateListRequest, HttpResponse.BodyHandlers.ofString());
    this.codes = deserializeStates(stateListResponse.body());
    stateMap = new HashMap<>();
    for (List<String> list : this.codes) {
      stateMap.put(list.get(0),list.get(1));
    }

  }

  public String getCode(String state) {
    if (stateMap.containsKey(state)){
      return stateMap.get(state);
    } else {
      throw new IllegalArgumentException("State " + state + " does not exist");
    }
  }

  public static List<List<String>> deserializeStates(String jsonList) throws IOException {
    List<List<String>> menu = new ArrayList<>();
    try {
      Moshi moshi = new Moshi.Builder().build();

      Type listType =
          Types.newParameterizedType(List.class, List.class); // nesting outside to inside
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

      List<List<String>> deserializedStates = adapter.fromJson(jsonList);

      return deserializedStates;
    }
    // From the Moshi Docs (https://github.com/square/moshi):
    //   "Moshi always throws a standard java.io.IOException if there is an error reading the JSON
    // document, or if it is malformed. It throws a JsonDataException if the JSON document is
    // well-formed, but doesn't match the expected format."
    catch (IOException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("OrderHandler: string wasn't valid JSON.");
      throw e;
    } catch (JsonDataException e) {
      // In a real system, we wouldn't println like this, but it's useful for demonstration:
      System.err.println("OrderHandler: JSON wasn't in the right format.");
      throw e;
    }
  }
}
