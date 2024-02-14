package edu.brown.cs.student.main.codes;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Codes {
  public static List<List<String>> deserializeCodes(String jsonList) throws IOException {
    List<List<String>> menu = new ArrayList<>();
    try {
      Moshi moshi = new Moshi.Builder().build();

      Type listType =
          Types.newParameterizedType(List.class, List.class); // nesting outside to inside
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listType);

      List<List<String>> deserializedStates = adapter.fromJson(jsonList);

      return deserializedStates;
    }
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
