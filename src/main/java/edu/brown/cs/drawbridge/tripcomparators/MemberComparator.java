package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

import java.util.List;

/**
 * A comparator that compares Lists of Trips based on how many Trips contain the
 * User as a member.
 *
 * @author Jeffrey Zhu
 */
public class MemberComparator implements ComparesSearchedTrips {

  private String userId;

  @Override public void setUserId(String userIdentification) {
    userId = userIdentification;
  }

  @Override public int compare(List<Trip> path1, List<Trip> path2) {
    int numberOfTripsMember1 = 0;
    int numberOfTripsMember2 = 0;
    for (Trip trip : path1) {
      if (trip.getMemberIds().contains(userId)) {
        numberOfTripsMember1++;
      }
    }
    for (Trip trip : path2) {
      if (trip.getMemberIds().contains(userId)) {
        numberOfTripsMember2++;
      }
    }
    return Integer.compare(numberOfTripsMember2, numberOfTripsMember1);
  }
}
