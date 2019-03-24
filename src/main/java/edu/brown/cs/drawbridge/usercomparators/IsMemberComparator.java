package edu.brown.cs.drawbridge.usercomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * A comparator that compares Users based on whether they are a member of a
 * given Trip.
 */
public class IsMemberComparator implements Comparator<User> {

  private Trip trip;

  /**
   * Creates a new IsMemberComparator using a Trip.
   *
   * @param trip
   *          A Trip used to compare Users.
   */
  public IsMemberComparator(Trip trip) {
    this.trip = trip;
  }

  @Override
  public int compare(User user1, User user2) {
    boolean firstIsMember = trip.getMemberIds().contains(user1.getId());
    boolean secondIsMember = trip.getMemberIds().contains(user2.getId());
    if (firstIsMember) {
      if (secondIsMember) {
        return 0;
      } else {
        return -1;
      }
    }
    if (secondIsMember) {
      return 1;
    }
    return 0;
  }

}
