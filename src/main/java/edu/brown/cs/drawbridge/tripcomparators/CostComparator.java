package edu.brown.cs.drawbridge.tripcomparators;

import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Lists of Trips based on the total cost per person.
 */
public class CostComparator implements ComparesSearchedTrips {

  private String userId;

  @Override
  public void setId(String userIdentification) {
    this.userId = userIdentification;
  }

  @Override
  public int compare(List<Trip> path1, List<Trip> path2) {
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
