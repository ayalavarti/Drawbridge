package edu.brown.cs.drawbridge.usercomparators;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * A comparator that compares Users based on whether they have requested to join
 * a given Trip.
 */
public class IsPendingComparator implements ComparesUsersInTrip {

  private Trip trip;

  @Override
  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  @Override
  public int compare(User user1, User user2) {
    boolean firstIsPending = trip.getPendingIds().contains(user1.getId());
    boolean secondIsPending = trip.getPendingIds().contains(user2.getId());
    return Boolean.compare(secondIsPending, firstIsPending);
  }
}
