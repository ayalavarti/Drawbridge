package edu.brown.cs.drawbridge.tripcomparators;

import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * A comparator that compares Lists of Trips based on the number of Trips in the
 * List.
 */
public class LengthComparator implements Comparator<List<Trip>> {

  @Override
  public int compare(List<Trip> path1, List<Trip> path2) {
    return Integer.compare(path1.size(), path2.size());
  }

}
