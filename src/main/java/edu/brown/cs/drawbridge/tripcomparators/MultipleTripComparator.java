package edu.brown.cs.drawbridge.tripcomparators;

import edu.brown.cs.drawbridge.models.Trip;

import java.util.Comparator;
import java.util.List;

/**
 * Comparator to compare two Lists of Trips using a list of comparators. If the
 * first compare method returns 0, then use the next comparator.
 *
 * @author Jeffrey Zhu
 */
public class MultipleTripComparator implements Comparator<List<Trip>> {

  private List<ComparesSearchedTrips> comparators;

  /**
   * MultipleComparator constructor.
   *
   * @param comparators
   *     List of comparators used to compare two searched Lists of Trips
   */
  public MultipleTripComparator(List<ComparesSearchedTrips> comparators) {
    this.comparators = comparators;
  }

  @Override public int compare(List<Trip> path1, List<Trip> path2) {
    for (int i = 0; i < comparators.size(); i++) {
      int currentRanking = comparators.get(i).compare(path1, path2);
      // If a comparator has sorted the two
      if (currentRanking != 0) {
        return currentRanking;
      }
      // Otherwise, loop to the next comparator
    }
    return 0;
  }
}
