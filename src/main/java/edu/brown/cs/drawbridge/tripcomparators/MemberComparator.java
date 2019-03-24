package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Trips based on whether or not it contains the User
 * as a member.
 */
public class MemberComparator implements Comparator<Trip> {

  private String userId;

  /**
   * Creates a new MemberComparator using the id of a User.
   *
   * @param userId
   *          The id of a User, used to compare Trips.
   */
  public MemberComparator(String userId) {
    this.userId = userId;
  }

  @Override
  public int compare(Trip t1, Trip t2) {
    boolean memberOfFirst = t1.getMemberIds().contains(userId);
    boolean memberOfSecond = t2.getMemberIds().contains(userId);
    if (memberOfFirst) {
      if (memberOfSecond) {
        return 0;
      } else {
        return -1;
      }
    }
    if (memberOfSecond) {
      return 1;
    }
    return 0;
  }

}
