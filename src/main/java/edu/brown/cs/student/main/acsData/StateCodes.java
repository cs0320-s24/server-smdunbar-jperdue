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
import java.util.List;

public class StateCodes {

  private final List<StateCodePair> codes;

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

    // What's the difference between these two lines? Why do we return the body? What is useful from
    // the raw response (hint: how can we use the status of response)?
    System.out.println(stateListResponse);
    System.out.println(stateListResponse.body());

    this.codes = deserializeStates(stateListResponse.body());
  }

  public int getCode(String state) {
    for (StateCodePair pair : this.codes) {
      if (pair.getState().equals(state)) {
        return pair.getStateCode();
      }
    }
    throw new IllegalArgumentException("State " + state + " does not exist");
  }

  public static List<StateCodePair> deserializeStates(String jsonList) throws IOException {
    List<StateCodePair> menu = new ArrayList<>();
    try {
      Moshi moshi = new Moshi.Builder().build(); // don't care what's happening here
      // notice the type and JSONAdapter parameterized type match the return type of the method
      // Since List is generic, we shouldn't just pass List.class to the adapter factory.
      // Instead, let's be more precise. Java has built-in classes for talking about generic types
      // programmatically.
      // Building libraries that use them is outside the scope of this class, but we'll follow the
      // Moshi docs'
      // template by creating a Type object corresponding to List<Ingredient>:
      Type listType =
          Types.newParameterizedType(List.class, StateCodePair.class); // nesting outside to inside
      JsonAdapter<List<StateCodePair>> adapter = moshi.adapter(listType);

      List<StateCodePair> deserializedStates = adapter.fromJson(jsonList);

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
