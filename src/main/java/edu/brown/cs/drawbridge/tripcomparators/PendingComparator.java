package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Trips based on whether or the User has requested
 * to join the Trip.
 */
public class PendingComparator implements ComparesSearchedTrips {

  private String userId;

  @Override
  public void setId(String userIdentification) {
    this.userId = userIdentification;
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
