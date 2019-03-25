package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Trips based on whether or not it contains the User
 * as a member.
 */
public class MemberComparator implements ComparesSearchedTrips {

  private String userId;

  @Override
  public void setId(String userIdentification) {
    this.userId = userIdentification;
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
