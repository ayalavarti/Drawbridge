package edu.brown.cs.drawbridge.tripcomparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * Tests HostComparator constructor and methods.
 */
public class HostComparatorTest {

  /**
   * Test HostComparator constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(new HostComparator("0"));
  }

  /**
   * Test compare method given two Trips both hosted by the User.
   */
  @Test
  public void testBothHosted() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    assertEquals(new HostComparator("0").compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given two Trips, neither hosted by the User.
   */
  @Test
  public void testNeitherHosted() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    assertEquals(new HostComparator("1").compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given the first hosted by the User.
   */
  @Test
  public void testFirstHosted() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "1", new LinkedList<String>(), new LinkedList<String>());
    assertEquals(new HostComparator("0").compare(trip1, trip2), -1);
  }

  /**
   * Test compare method given the second hosted by the User.
   */
  @Test
  public void testSecondHosted() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "1", new LinkedList<String>(), new LinkedList<String>());
    assertEquals(new HostComparator("1").compare(trip1, trip2), 1);
  }

  /**
   * Test HostComparator by sorting multiple Trips.
   */
  @Test
  public void testMultipleTrips() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "1", new LinkedList<String>(), new LinkedList<String>());

    ArrayList<Trip> trips = new ArrayList<Trip>();
    trips.add(trip1);
    trips.add(trip2);
    trips.add(trip3);
    ArrayList<Trip> expected = new ArrayList<Trip>();
    expected.add(trip3);
    expected.add(trip1);
    expected.add(trip2);
    Collections.sort(trips, new HostComparator("1"));
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(trips.get(i), expected.get(i));
    }
  }
}
