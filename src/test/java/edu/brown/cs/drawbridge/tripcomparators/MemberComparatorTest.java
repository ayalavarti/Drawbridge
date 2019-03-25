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
 * Tests MemberComparator constructor and methods.
 */
public class MemberComparatorTest {

  /**
   * Test MemberComparator constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(new MemberComparator());
  }

  /**
   * Test compare method given two Trips with User as member.
   */
  @Test
  public void testBothMember() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    ComparesSearchedTrips memberComparator = new MemberComparator();
    memberComparator.setId("10");
    assertEquals(memberComparator.compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given two Trips, neither with User as member.
   */
  @Test
  public void testNeitherMember() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    ComparesSearchedTrips memberComparator = new MemberComparator();
    memberComparator.setId("0");
    assertEquals(memberComparator.compare(trip1, trip2), 0);
  }

  /**
   * Test compare method given the first with User as member.
   */
  @Test
  public void testFirstMember() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    ComparesSearchedTrips memberComparator = new MemberComparator();
    memberComparator.setId("10");
    assertEquals(memberComparator.compare(trip1, trip2), -1);
  }

  /**
   * Test compare method given the second with User as member.
   */
  @Test
  public void testSecondMember() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, new LinkedList<String>());
    ComparesSearchedTrips memberComparator = new MemberComparator();
    memberComparator.setId("10");
    assertEquals(memberComparator.compare(trip1, trip2), 1);
  }

  /**
   * Test MemberComparator by sorting multiple Trips.
   */
  @Test
  public void testMultipleTrips() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("10");
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
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("1", memberIds, new LinkedList<String>());

    ArrayList<Trip> trips = new ArrayList<Trip>();
    trips.add(trip1);
    trips.add(trip2);
    trips.add(trip3);
    ArrayList<Trip> expected = new ArrayList<Trip>();
    expected.add(trip3);
    expected.add(trip1);
    expected.add(trip2);

    ComparesSearchedTrips memberComparator = new MemberComparator();
    memberComparator.setId("10");
    Collections.sort(trips, memberComparator);
    for (int i = 0; i < expected.size(); i++) {
      assertEquals(trips.get(i), expected.get(i));
    }
  }
}
