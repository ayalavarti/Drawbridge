package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * Comparator to compare two Trips using a list of comparators. If the first
 * compare method returns 0, then use the next comparator.
 */
public class MultipleTripComparator implements Comparator<Trip> {

  private List<ComparesSearchedTrips> comparators;

  /**
   * MultipleComparator constructor.
   *
   * @param comparators
   *          List of comparators used to compare two searched Trips
   */
  public MultipleTripComparator(List<ComparesSearchedTrips> comparators) {
    this.comparators = comparators;
  }

  @Override
  public int compare(Trip trip1, Trip trip2) {
    for (int i = 0; i < comparators.size(); i++) {
      int currentRanking = comparators.get(i).compare(trip1, trip2);
      // If a comparator has sorted the two
      if (currentRanking != 0) {
        return currentRanking;
      }
      // Otherwise, loop to the next comparator
    }
    return 0;
  }
}
