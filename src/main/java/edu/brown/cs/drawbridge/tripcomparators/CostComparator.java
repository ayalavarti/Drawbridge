package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

import java.util.List;

/**
 * A comparator that compares Lists of Trips based on the total cost per person.
 *
 * @author Jeffrey Zhu
 */
public class CostComparator implements ComparesSearchedTrips {

  private String userId;

  @Override public void setUserId(String userIdentification) {
    userId = userIdentification;
  }

  @Override public int compare(List<Trip> path1, List<Trip> path2) {
    double totalCostPerUser1 = 0;
    double totalCostPerUser2 = 0;
    for (Trip trip : path1) {
      totalCostPerUser1 += trip.getCostPerUser(userId);
    }
    for (Trip trip : path2) {
      totalCostPerUser2 += trip.getCostPerUser(userId);
    }
    return Double.compare(totalCostPerUser1, totalCostPerUser2);
  }
}
