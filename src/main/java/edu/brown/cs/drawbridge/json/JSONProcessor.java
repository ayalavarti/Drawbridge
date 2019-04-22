package edu.brown.cs.drawbridge.json;

import com.google.gson.JsonArray;
import edu.brown.cs.drawbridge.models.Trip;

import java.util.List;

/**
 * Class to process our data structures into JSON.
 * @author Mark Lavrentyev
 */
public abstract class JSONProcessor {

  /**
   * Private constructor to hide default public one.
   */
  private JSONProcessor() {}

  /**
   * Method for processing trip groups into json with a user to set the
   * status for.
   * @param tripGroups The trip groups to process.
   * @param uid The user to set the status field in the trips for.
   * @return A json representation of the trips groups.
   */
  public static JsonArray processTripGroups(List<List<Trip>> tripGroups,
                                            String uid) {
    JsonArray data = new JsonArray();

    for (List<Trip> tripGroup : tripGroups) {
      JsonArray pTripGroup = new JsonArray();
      for (Trip trip : tripGroup) {
        pTripGroup.add(trip.toJson(uid));
      }
      data.add(pTripGroup);
    }

    return data;
  }

  /**
   * Method to process trip groups into json without a user. Status field
   * will be set to "join" for all trips.
   * @param tripGroups The trips to process.
   * @return A json representation of the trips.
   */
  public static JsonArray processTripGroups(List<List<Trip>> tripGroups) {
    return processTripGroups(tripGroups, "");
  }
}
