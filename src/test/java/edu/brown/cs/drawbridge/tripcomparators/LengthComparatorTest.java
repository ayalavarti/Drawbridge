package edu.brown.cs.drawbridge.tripcomparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;

/**
 * Test LengthComparator methods and constructor.
 */
public class LengthComparatorTest {

  /**
   * Test LengthComparator constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(new LengthComparator());
  }

  /**
   * Test compare method given two paths with equal number of Trips.
   */
  @Test
  public void testEqualSize() {
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

    assertEquals(new LengthComparator().compare(path1, path2), 0);
  }

  /**
   * Test compare method given the first path has more Trips.
   */
  @Test
  public void testFirstMore() {
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
    List<Trip> path1 = new ArrayList<Trip>(Arrays.asList(trip1, trip2, trip3));
    List<Trip> path2 = new ArrayList<Trip>(Arrays.asList(trip4));

    assertEquals(new LengthComparator().compare(path1, path2), 1);
  }

  /**
   * Test compare method given the second path has more Trips..
   */
  @Test
  public void testSecondMore() {
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
    List<Trip> path1 = new ArrayList<Trip>(Arrays.asList(trip1));
    List<Trip> path2 = new ArrayList<Trip>(Arrays.asList(trip2, trip3, trip4));

    assertEquals(new LengthComparator().compare(path1, path2), -1);
  }

}
