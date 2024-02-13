package edu.brown.cs.student.main.acsData;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StateInfoUtilities {
  public static List deserializeStateInfo(String json) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an Activity class then uses it to parse the JSON.
      Type listType = Types.newParameterizedType(List.class, List.class);
      JsonAdapter<List<List>> adapter = moshi.adapter(listType);

      List<List> stateInfo = adapter.fromJson(json);

      return stateInfo;
    }
    // Returns an empty activity... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      e.printStackTrace();
      return new ArrayList();
    }
  }
}
