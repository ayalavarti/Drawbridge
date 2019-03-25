package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * An interface to compare searched Trips.
 */
public interface ComparesSearchedTrips extends Comparator<Trip> {

  /**
   * Set the id of the User, used to compare searched Trips.
   *
   * @param userId
   *          The id of the User used to compare searched Trips
   */
  void setId(String userId);

}
