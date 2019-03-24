package edu.brown.cs.drawbridge.usercomparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * Tests IsHostComparator constructor and methods.
 */
public class IsHostComparatorTest {

  /**
   * Test IsHostComparator constructor.
   */
  @Test
  public void testConstructor() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);
    assertNotNull(new IsHostComparator(trip));
  }

  /**
   * Test compare method given neither User hosting the Trip.
   */
  @Test
  public void testNeitherHosting() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);

    User user1 = new User("10", "name1", "email");
    User user2 = new User("11", "name1", "email");
    assertEquals(new IsHostComparator(trip).compare(user1, user2), 0);
  }

  /**
   * Test compare method given the first User hosting the Trip.
   */
  @Test
  public void testFirstHosting() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);

    User user1 = new User("0", "name1", "email");
    User user2 = new User("11", "name1", "email");
    assertEquals(new IsHostComparator(trip).compare(user1, user2), -1);
  }

  /**
   * Test compare method given the second User hosting the Trip.
   */
  @Test
  public void testSecondHosting() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("2");
    Trip trip = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1d234567890", "My car", "comments")
        .buildWithUsers("0", memberIds, pendingIds);

    User user1 = new User("10", "name1", "email");
    User user2 = new User("0", "name1", "email");
    assertEquals(new IsHostComparator(trip).compare(user1, user2), 1);
  }
}
