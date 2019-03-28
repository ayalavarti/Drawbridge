package edu.brown.cs.drawbridge.tripcomparators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * Tests MultipleTripComparator constructor and methods.
 */
public class MultipleTripComparatorTest {

  /**
   * Test MultipleTripComparator constructor.
   */
  @Test
  public void testConstructor() {
    List<ComparesSearchedTrips> comparators;
    comparators = new ArrayList<ComparesSearchedTrips>();
    assertNotNull(new MultipleTripComparator(comparators));
  }

  /**
   * Test compare method given one path contains the User as host more
   * frequently.
   */
  @Test
  public void testHost() {
    List<ComparesSearchedTrips> comparators;
    comparators = new ArrayList<ComparesSearchedTrips>(
        Arrays.asList(new HostComparator(), new MemberComparator(),
            new PendingComparator(), new CostComparator()));
    String userId = "0";
    for (ComparesSearchedTrips comparator : comparators) {
      comparator.setUserId(userId);
    }
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(3, "name3")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("1", memberIds, new LinkedList<String>());
    Trip trip4 = Trip.TripBuilder.newTripBuilder().addIdentification(4, "name4")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    List<Trip> path1 = new ArrayList<Trip>(Arrays.asList(trip1, trip2));
    List<Trip> path2 = new ArrayList<Trip>(Arrays.asList(trip3, trip4));

    assertTrue(c.compare(path1, path2) < 0);
    assertTrue(c.compare(path2, path1) > 0);
  }

  /**
   * Test compare method given one path contains the User as member more
   * frequently.
   */
  @Test
  public void testMember() {
    List<ComparesSearchedTrips> comparators;
    comparators = new ArrayList<ComparesSearchedTrips>(
        Arrays.asList(new HostComparator(), new MemberComparator(),
            new PendingComparator(), new CostComparator()));
    String userId = "10";
    for (ComparesSearchedTrips comparator : comparators) {
      comparator.setUserId(userId);
    }
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(3, "name3")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip4 = Trip.TripBuilder.newTripBuilder().addIdentification(4, "name4")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    List<Trip> path1 = new ArrayList<Trip>(Arrays.asList(trip1, trip2));
    List<Trip> path2 = new ArrayList<Trip>(Arrays.asList(trip3, trip4));

    assertTrue(c.compare(path1, path2) < 0);
    assertTrue(c.compare(path2, path1) > 0);
  }

  /**
   * Test compare method given a Trip that contains User as pending.
   */
  @Test
  public void testPending() {
    List<ComparesSearchedTrips> comparators;
    comparators = new ArrayList<ComparesSearchedTrips>(
        Arrays.asList(new HostComparator(), new MemberComparator(),
            new PendingComparator(), new CostComparator()));
    String userId = "10";
    for (ComparesSearchedTrips comparator : comparators) {
      comparator.setUserId(userId);
    }
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(), pendingIds);
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(), pendingIds);
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(3, "name3")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip4 = Trip.TripBuilder.newTripBuilder().addIdentification(4, "name4")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(), pendingIds);
    List<Trip> path1 = new ArrayList<Trip>(Arrays.asList(trip1, trip2));
    List<Trip> path2 = new ArrayList<Trip>(Arrays.asList(trip3, trip4));

    assertTrue(c.compare(path1, path2) < 0);
    assertTrue(c.compare(path2, path1) > 0);
  }

  /**
   * Test compare method given a Trip with cost higher than the other.
   */
  @Test
  public void testCost() {
    List<ComparesSearchedTrips> comparators;
    comparators = new ArrayList<ComparesSearchedTrips>(
        Arrays.asList(new HostComparator(), new MemberComparator(),
            new PendingComparator(), new CostComparator()));
    String userId = "10";
    for (ComparesSearchedTrips comparator : comparators) {
      comparator.setUserId(userId);
    }
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 99, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(3, "name3")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip4 = Trip.TripBuilder.newTripBuilder().addIdentification(4, "name4")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    List<Trip> path1 = new ArrayList<Trip>(Arrays.asList(trip1, trip2));
    List<Trip> path2 = new ArrayList<Trip>(Arrays.asList(trip3, trip4));

    assertTrue(c.compare(path1, path2) < 0);
    assertTrue(c.compare(path2, path1) > 0);
  }
}
