package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * An interface to compare searched Lists of Trips.
 */
public interface ComparesSearchedTrips extends Comparator<List<Trip>> {

  /**
   * Set the id of the User, used to compare searched lists of Trips.
   *
   * @param userId
   *          The id of the User used to compare searched lists of Trips
   */
  void setUserId(String userId);

}
