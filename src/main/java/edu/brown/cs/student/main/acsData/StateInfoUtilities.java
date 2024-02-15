package edu.brown.cs.student.main.acsData;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.api_exceptions.BadJsonException;
import edu.brown.cs.student.main.api_exceptions.BadRequestException;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class StateInfoUtilities {
  public static List deserializeStateInfo(String json)
      throws IOException, BadRequestException, BadJsonException {
    Moshi moshi = new Moshi.Builder().build();
    try {
      Type listType = Types.newParameterizedType(List.class, List.class);

      JsonAdapter<List<List>> adapter = moshi.adapter(listType);

      List<List> stateInfo = adapter.fromJson(json);

      return stateInfo;
    } catch (JsonDataException e) {
      throw new BadJsonException("error_bad_json");
    } catch (EOFException e) {
      throw new BadRequestException("error_bad_request");
    }
  }
}
