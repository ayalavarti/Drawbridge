package edu.brown.cs.drawbridge.usercomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * An interface used to compare Users within a Trip.
 */
public interface ComparesUsersInTrip extends Comparator<User> {

  /**
   * Set Trip data, used to compare Users within the Trip.
   *
   * @param trip
   *          The Trip that contains Users to compare
   */
  void setTrip(Trip trip);

}
