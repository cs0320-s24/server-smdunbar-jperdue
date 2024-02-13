package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;

public class StateInfoUtilities {
  public static StateInfo deserializeStateInfo(String json) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an Activity class then uses it to parse the JSON.
      JsonAdapter<StateInfo> adapter = moshi.adapter(StateInfo.class);

      StateInfo stateInfo = adapter.fromJson(json);

      return stateInfo;
    }
    // Returns an empty activity... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      e.printStackTrace();
      return new StateInfo();
    }
  }
}
