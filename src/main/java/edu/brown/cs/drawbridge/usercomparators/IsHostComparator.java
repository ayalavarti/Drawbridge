package edu.brown.cs.drawbridge.usercomparators;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * A comparator that compares Users based on whether they host a given Trip.
 */
public class IsHostComparator implements ComparesUsersInTrip {

  private Trip trip;

  @Override
  public void setTrip(Trip trip) {
    this.trip = trip;
  }

  @Override
  public int compare(User user1, User user2) {
    boolean firstIsHost = trip.getHostId().equals(user1.getId());
    boolean secondIsHost = trip.getHostId().equals(user2.getId());
    return Boolean.compare(secondIsHost, firstIsHost);
  }
}
