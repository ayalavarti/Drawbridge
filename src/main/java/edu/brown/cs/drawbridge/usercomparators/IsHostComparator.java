package edu.brown.cs.drawbridge.usercomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * A comparator that compares Users based on whether they host a given Trip.
 */
public class IsHostComparator implements Comparator<User> {

  private Trip trip;

  /**
   * Creates a new IsHostComparator using a Trip.
   *
   * @param trip
   *          A Trip used to compare Users.
   */
  public IsHostComparator(Trip trip) {
    this.trip = trip;
  }

  @Override
  public int compare(User user1, User user2) {
    boolean firstIsHost = trip.getHostId().equals(user1.getId());
    boolean secondIsHost = trip.getHostId().equals(user2.getId());
    if (firstIsHost) {
      return -1;
    }
    if (secondIsHost) {
      return 1;
    }
    return 0;
  }
}
