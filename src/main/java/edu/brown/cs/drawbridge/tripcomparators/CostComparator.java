package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Trips based on the cost per person.
 */
public class CostComparator implements Comparator<Trip> {

  private String userId;

  /**
   * Creates a new CostComparator using the id of a User.
   *
   * @param userId
   *          The id of a User, used to compare Trips.
   */
  public CostComparator(String userId) {
    this.userId = userId;
  }

  @Override
  public int compare(Trip t1, Trip t2) {
    return Double.compare(t1.getCostPerUser(userId), t2.getCostPerUser(userId));
  }

}
