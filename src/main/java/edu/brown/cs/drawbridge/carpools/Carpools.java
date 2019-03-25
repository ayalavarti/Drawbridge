package edu.brown.cs.drawbridge.carpools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.tripcomparators.TimeComparator;
import edu.brown.cs.drawbridge.usercomparators.ComparesUsersInTrip;
import edu.brown.cs.drawbridge.usercomparators.IsHostComparator;
import edu.brown.cs.drawbridge.usercomparators.IsMemberComparator;
import edu.brown.cs.drawbridge.usercomparators.IsPendingComparator;
import edu.brown.cs.drawbridge.usercomparators.MultipleUserComparator;

public class Carpools {

  private static final TripSearcher TRIP_SEARCHER = new TripSearcher();
  private static final Comparator<Trip> TIME_COMPARATOR = new TimeComparator();

  private static final List<ComparesUsersInTrip> COMPARATORS = Arrays.asList(
      new IsHostComparator(), new IsMemberComparator(),
      new IsPendingComparator());
  private static final MultipleUserComparator USER_COMPARATOR = new MultipleUserComparator(
      COMPARATORS);

  private void setTrip(Trip trip) {
    for (ComparesUsersInTrip comparator : COMPARATORS) {
      comparator.setTrip(trip);
    }
  }

}
