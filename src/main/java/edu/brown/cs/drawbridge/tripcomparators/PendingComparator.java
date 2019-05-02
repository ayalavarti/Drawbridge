package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Lists of Trips based on how many Trips the User
 * has requested to join.
 */
public class PendingComparator implements Comparator<List<Trip>>, Identifiable {

  private String userId;

  @Override
  public void setUserId(String userIdentification) {
    userId = userIdentification;
  }

  @Override
  public int compare(List<Trip> path1, List<Trip> path2) {
    int numberOfTripsPending1 = 0;
    int numberOfTripsPending2 = 0;
    for (Trip trip : path1) {
      if (trip.getPendingIds().contains(userId)) {
        numberOfTripsPending1++;
      }
    }
    for (Trip trip : path2) {
      if (trip.getPendingIds().contains(userId)) {
        numberOfTripsPending2++;
      }
    }
    return Integer.compare(numberOfTripsPending2, numberOfTripsPending1);
  }
}
