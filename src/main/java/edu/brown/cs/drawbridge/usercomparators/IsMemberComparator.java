package edu.brown.cs.drawbridge.usercomparators;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * A comparator that compares Users based on whether they are a member of a
 * given Trip.
 */
public class IsMemberComparator implements ComparesUsersInTrip {

  private Trip trip;

  @Override
  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  @Override
  public int compare(User user1, User user2) {
    boolean firstIsMember = trip.getMemberIds().contains(user1.getId());
    boolean secondIsMember = trip.getMemberIds().contains(user2.getId());
    return Boolean.compare(secondIsMember, firstIsMember);
  }

}
