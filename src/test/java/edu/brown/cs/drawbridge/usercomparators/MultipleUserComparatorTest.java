package edu.brown.cs.drawbridge.usercomparators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

public class MultipleUserComparatorTest {

  /**
   * Test MultipleUserComparator constructor.
   */
  @Test
  public void testConstructor() {
    List<ComparesUsersInTrip> comparators;
    comparators = new ArrayList<ComparesUsersInTrip>();
    assertNotNull(new MultipleUserComparator(comparators));
  }

  /**
   * Test compare method given a User hosts the Trip.
   */
  @Test
  public void testHosting() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);
    List<ComparesUsersInTrip> comparators = new ArrayList<ComparesUsersInTrip>(
        Arrays.asList(new IsHostComparator(), new IsMemberComparator(),
            new IsPendingComparator()));
    for (ComparesUsersInTrip comparator : comparators) {
      comparator.setTrip(trip);
    }
    MultipleUserComparator c = new MultipleUserComparator(comparators);

    User user1 = new User("0", "name1", "email");
    User user2 = new User("11", "name1", "email");

    assertTrue(c.compare(user1, user2) < 0);
    assertTrue(c.compare(user2, user1) > 0);
  }

  /**
   * Test compare method given a User is a member of a Trip.
   */
  @Test
  public void testMember() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);
    List<ComparesUsersInTrip> comparators = new ArrayList<ComparesUsersInTrip>(
        Arrays.asList(new IsHostComparator(), new IsMemberComparator(),
            new IsPendingComparator()));
    for (ComparesUsersInTrip comparator : comparators) {
      comparator.setTrip(trip);
    }
    MultipleUserComparator c = new MultipleUserComparator(comparators);

    User user1 = new User("1", "name1", "email");
    User user2 = new User("11", "name1", "email");

    assertTrue(c.compare(user1, user2) < 0);
    assertTrue(c.compare(user2, user1) > 0);
  }

  /**
   * Test compare method given a User has requested to join the trip.
   */
  @Test
  public void testPending() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);
    List<ComparesUsersInTrip> comparators = new ArrayList<ComparesUsersInTrip>(
        Arrays.asList(new IsHostComparator(), new IsMemberComparator(),
            new IsPendingComparator()));
    for (ComparesUsersInTrip comparator : comparators) {
      comparator.setTrip(trip);
    }
    MultipleUserComparator c = new MultipleUserComparator(comparators);

    User user1 = new User("2", "name1", "email");
    User user2 = new User("11", "name1", "email");

    assertTrue(c.compare(user1, user2) < 0);
    assertTrue(c.compare(user2, user1) > 0);
  }
}
