package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Trips based on the cost per person.
 */
public class CostComparator implements ComparesSearchedTrips {

  private String userId;

  @Override
  public void setId(String userIdentification) {
    this.userId = userIdentification;
  }

  @Override
  public int compare(Trip t1, Trip t2) {
    return Double.compare(t1.getCostPerUser(userId), t2.getCostPerUser(userId));
  }
}
