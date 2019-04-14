package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

import java.util.Comparator;
import java.util.List;

/**
 * An interface to compare searched Lists of Trips.
 *
 * @author Jeffrey Zhu
 */
public interface ComparesSearchedTrips extends Comparator<List<Trip>> {

  /**
   * Set the id of the User, used to compare searched lists of Trips.
   *
   * @param userId
   *     The id of the User used to compare searched lists of Trips
   */
  void setUserId(String userId);

}
