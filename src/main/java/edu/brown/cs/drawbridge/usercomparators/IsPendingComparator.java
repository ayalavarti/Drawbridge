package edu.brown.cs.drawbridge.usercomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * A comparator that compares Users based on whether they have requested to join
 * a given Trip.
 */
public class IsPendingComparator implements Comparator<User> {

  private Trip trip;

  /**
   * Creates a new IsPendingComparator using a Trip.
   *
   * @param trip
   *          A Trip used to compare Users.
   */
  public IsPendingComparator(Trip trip) {
    this.trip = trip;
  }

  @Override
  public int compare(User user1, User user2) {
    boolean firstIsPending = trip.getPendingIds().contains(user1.getId());
    boolean secondIsPending = trip.getPendingIds().contains(user2.getId());
    if (firstIsPending) {
      if (secondIsPending) {
        return 0;
      } else {
        return -1;
      }
    }
    if (secondIsPending) {
      return 1;
    }
    return 0;
  }
}
