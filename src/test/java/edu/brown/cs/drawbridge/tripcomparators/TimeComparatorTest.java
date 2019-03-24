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
 * Tests TimeComparator constructor and methods.
 */
public class TimeComparatorTest {

  /**
   * Test TimeComparator constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(new TimeComparator());
  }

  /**
   * Test compare method given two Trips with the same departure time.
   */
  @Test
  public void testBothPending() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").build();
    assertEquals(new TimeComparator().compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given the first departure time is larger.
   */
  @Test
  public void testFirstPending() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(15, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").build();
    assertEquals(new TimeComparator().compare(trip1, trip2), 1);
  }

  /**
   * Test compare method given the second departure time is smaller.
   */
  @Test
  public void testSecondPending() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(15, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").build();
    assertEquals(new TimeComparator().compare(trip1, trip2), -1);
  }

  /**
   * Test TimeComparator by sorting multiple Trips.
   */
  @Test
  public void testMultipleTrips() {
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(11, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(9, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(12, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("1", new LinkedList<String>(), pendingIds);

    ArrayList<Trip> trips = new ArrayList<Trip>();
    trips.add(trip1);
    trips.add(trip2);
    trips.add(trip3);
    ArrayList<Trip> expected = new ArrayList<Trip>();
    expected.add(trip2);
    expected.add(trip1);
    expected.add(trip3);
    Collections.sort(trips, new TimeComparator());
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(trips.get(i), expected.get(i));
    }
  }
}
