package edu.brown.cs.drawbridge.tripcomparators;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
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
    List<Comparator<Trip>> comparators = new ArrayList<Comparator<Trip>>();
    assertNotNull(new MultipleTripComparator(comparators));
  }

  /**
   * Test compare method given a Trip contains the User as host.
   */
  @Test
  public void testHost() {
    List<Comparator<Trip>> comparators = new ArrayList<Comparator<Trip>>();
    String userId = "0";
    comparators.add(new HostComparator(userId));
    comparators.add(new MemberComparator(userId));
    comparators.add(new PendingComparator(userId));
    comparators.add(new CostComparator(userId));
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "1", new LinkedList<String>(), new LinkedList<String>());

    assertTrue(c.compare(trip1, trip2) < 0);
    assertTrue(c.compare(trip2, trip1) > 0);
  }

  /**
   * Test compare method given a Trip that contains the User as member.
   */
  @Test
  public void testMember() {
    List<Comparator<Trip>> comparators = new ArrayList<Comparator<Trip>>();
    String userId = "10";
    comparators.add(new HostComparator(userId));
    comparators.add(new MemberComparator(userId));
    comparators.add(new PendingComparator(userId));
    comparators.add(new CostComparator(userId));
    MultipleTripComparator c = new MultipleTripComparator(comparators);
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

    assertTrue(c.compare(trip1, trip2) < 0);
    assertTrue(c.compare(trip2, trip1) > 0);
  }

  /**
   * Test compare method given a Trip that contains User as pending.
   */
  @Test
  public void testPending() {
    List<Comparator<Trip>> comparators = new ArrayList<Comparator<Trip>>();
    String userId = "10";
    comparators.add(new HostComparator(userId));
    comparators.add(new MemberComparator(userId));
    comparators.add(new PendingComparator(userId));
    comparators.add(new CostComparator(userId));
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("10");
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("0", new LinkedList<String>(), pendingIds);
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());

    assertTrue(c.compare(trip1, trip2) < 0);
    assertTrue(c.compare(trip2, trip1) > 0);
  }

  /**
   * Test compare method given a Trip with cost higher than the other.
   */
  @Test
  public void testCost() {
    List<Comparator<Trip>> comparators = new ArrayList<Comparator<Trip>>();
    String userId = "0";
    comparators.add(new HostComparator(userId));
    comparators.add(new MemberComparator(userId));
    comparators.add(new PendingComparator(userId));
    comparators.add(new CostComparator(userId));
    MultipleTripComparator c = new MultipleTripComparator(comparators);
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 0, 0).addTimes(10, 20)
        .addDetails(5, 150, "1234567890", "My car", "comments").buildWithUsers(
            "0", new LinkedList<String>(), new LinkedList<String>());

    assertTrue(c.compare(trip1, trip2) < 0);
    assertTrue(c.compare(trip2, trip1) > 0);
  }
}
