package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Trips based on whether or the User has requested
 * to join the Trip.
 */
public class PendingComparator implements Comparator<Trip> {

  private String userId;

  /**
   * Creates a new PendingComparator using the id of a User.
   *
   * @param userId
   *          The id of a User, used to compare Trips.
   */
  public PendingComparator(String userId) {
    this.userId = userId;
  }

  @Override
  public int compare(Trip t1, Trip t2) {
    boolean pendingOfFirst = t1.getPendingIds().contains(userId);
    boolean pendingOfSecond = t2.getPendingIds().contains(userId);
    if (pendingOfFirst) {
      if (pendingOfSecond) {
        return 0;
      } else {
        return -1;
      }
    }
    if (pendingOfSecond) {
      return 1;
    }
    return 0;
  }
}
