package edu.brown.cs.drawbridge.carpools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.tripcomparators.ComparesSearchedTrips;
import edu.brown.cs.drawbridge.tripcomparators.CostComparator;
import edu.brown.cs.drawbridge.tripcomparators.HostComparator;
import edu.brown.cs.drawbridge.tripcomparators.MemberComparator;
import edu.brown.cs.drawbridge.tripcomparators.MultipleTripComparator;
import edu.brown.cs.drawbridge.tripcomparators.PendingComparator;

public class TripSearcher {

  private static final List<ComparesSearchedTrips> COMPARATORS = Arrays.asList(
      new HostComparator(), new MemberComparator(), new PendingComparator(),
      new CostComparator());
  private static final MultipleTripComparator TRIP_COMPARATOR = new MultipleTripComparator(
      COMPARATORS);

  private void setUser(String userId) {
    for (ComparesSearchedTrips comparator : COMPARATORS) {
      comparator.setId(userId);
    }
  }

  public List<Trip> search(String userId, double startLat, double startLon,
      double endLat, double endLon, int departureTime, double distanceRadius,
      int timeRadius) {
    setUser(userId);
    return new LinkedList<Trip>();
  }

}
