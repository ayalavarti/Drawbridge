package edu.brown.cs.drawbridge.tripcomparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * Tests PendingComparator constructor and methods.
 */
public class PendingComparatorTest {

  /**
   * Test PendingComparator constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(new PendingComparator());
  }

  /**
   * Test compare method given two Trips with User as pending.
   */
  @Test
  public void testBothPending() {
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
    ComparesSearchedTrips pendingComparator = new PendingComparator();
    pendingComparator.setId("10");
    assertEquals(pendingComparator.compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given two Trips, neither with User as pending.
   */
  @Test
  public void testNeitherPending() {
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
    ComparesSearchedTrips pendingComparator = new PendingComparator();
    pendingComparator.setId("11");
    assertEquals(pendingComparator.compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given the first with User as pending.
   */
  @Test
  public void testFirstPending() {
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(), pendingIds);
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    ComparesSearchedTrips pendingComparator = new PendingComparator();
    pendingComparator.setId("10");
    assertEquals(pendingComparator.compare(trip1, trip2), -1);
  }

  /**
   * Test compare method given the second with User as pending.
   */
  @Test
  public void testSecondPending() {
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(), pendingIds);
    ComparesSearchedTrips pendingComparator = new PendingComparator();
    pendingComparator.setId("10");
    assertEquals(pendingComparator.compare(trip1, trip2), 1);
  }

  /**
   * Test PendingComparator by sorting multiple Trips.
   */
  @Test
  public void testMultipleTrips() {
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(),
            new LinkedList<String>());
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("1", new LinkedList<String>(), pendingIds);

    ArrayList<Trip> trips = new ArrayList<Trip>();
    trips.add(trip1);
    trips.add(trip2);
    trips.add(trip3);
    ArrayList<Trip> expected = new ArrayList<Trip>();
    expected.add(trip3);
    expected.add(trip1);
    expected.add(trip2);

    ComparesSearchedTrips pendingComparator = new PendingComparator();
    pendingComparator.setId("10");
    Collections.sort(trips, pendingComparator);
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(trips.get(i), expected.get(i));
    }
  }
}
