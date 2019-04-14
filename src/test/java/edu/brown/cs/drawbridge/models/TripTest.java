package edu.brown.cs.drawbridge.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Tests the Trip constructor and methods.
 */
public class TripTest {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    assertNotNull(Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 1, 2, 3).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build());
    assertNotNull(Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 1, 2, 3).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds));
  }

  /**
   * Test getId method.
   */
  @Test
  public void testGetId() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 1, 2, 3).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getId(), 0);
  }

  /**
   * Test getName method.
   */
  @Test
  public void testGetName() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(0, 1, 2, 3).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getName(), "name");
  }

  /**
   * Test getStartingLatitude method.
   */
  @Test
  public void testGetStartingLatitude() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getStartingLatitude(), 5, 0.1);
  }

  /**
   * Test getStartingLongitude method.
   */
  @Test
  public void testGetStartingLongitude() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getStartingLongitude(), 6, 0.1);
  }

  /**
   * Test getStartingLongitude method.
   */
  @Test
  public void testGetEndingLatitude() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getEndingLatitude(), 7, 0.1);
  }

  /**
   * Test getStartingLongitude method.
   */
  @Test
  public void testGetEndingLongitude() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getEndingLongitude(), 8, 0.1);
  }

  /**
   * Test getStartingAddress method.
   */
  @Test
  public void testGetStartingAddress() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getStartingAddress(), "start");
  }

  /**
   * Test getEndingAddress method.
   */
  @Test
  public void testGetEndingAddress() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getEndingAddress(), "end");
  }

  /**
   * Test getDepartureTime method.
   */
  @Test
  public void testGetDepartureTime() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getDepartureTime(), 10);
  }

  /**
   * Test getGetEta method.
   */
  @Test
  public void testGetEta() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getEta(), 20);
  }

  /**
   * Test getMaxUsers method.
   */
  @Test
  public void testGetMaxUsers() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getMaxUsers(), 5);
  }

  /**
   * Test getCost method.
   */
  @Test
  public void testGetCost() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 100, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getCost(), 100.0, 0.1);
  }

  /**
   * Test getPhoneNumber method.
   */
  @Test
  public void testGetPhoneNumber() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getPhoneNumber(), "1234567890");
  }

  /**
   * Test getMethodOfTransportation method.
   */
  @Test
  public void testGetMethodOfTransportation() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getMethodOfTransportation(), "My car");
  }

  /**
   * Test getComments method.
   */
  @Test
  public void testGetComments() {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(0, "name")
        .addLocations(5, 6, 7, 8).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(t.getComments(), "comments");
  }

  /**
   * Test getHostId method given a Trip without User information.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetHostIdNull() {
    Trip noUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(5, 6, 7, 8)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments").build();
    noUsers.getHostId();
  }

  /**
   * Test getMemberIds method given a Trip without User information.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetMemberIdsNull() {
    Trip noUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(5, 6, 7, 8)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments").build();
    noUsers.getMemberIds();
  }

  /**
   * Test getPendingIds method given a Trip without User information.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPendingIdsNull() {
    Trip noUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(5, 6, 7, 8)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments").build();
    noUsers.getPendingIds();
  }

  /**
   * Test getHostId method given a Trip with User information.
   */
  @Test
  public void testGetHostId() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip hasUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(0, 1, 2, 3)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds);
    assertEquals(hasUsers.getHostId(), "host");
  }

  /**
   * Test getMemberIds method given a Trip with User information.
   */
  @Test
  public void testGetMemberIds() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip hasUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(0, 1, 2, 3)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds);
    assertEquals(hasUsers.getHostId(), "host");
    for (int i = 0; i < memberIds.size(); i++) {
      assertEquals(hasUsers.getMemberIds().get(i), memberIds.get(i));
    }
  }

  /**
   * Test getPendingIds method given a Trip with User information.
   */
  @Test
  public void testGetPendingIds() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip hasUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(0, 1, 2, 3)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds);
    assertEquals(hasUsers.getHostId(), "host");
    for (int i = 0; i < memberIds.size(); i++) {
      assertEquals(hasUsers.getPendingIds().get(i), pendingIds.get(i));
    }
  }

  /**
   * Test getCurrentSize method given a Trip without User information.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetCurrentSizeNull() {
    Trip noUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(5, 6, 7, 8)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments").build();
    noUsers.getCurrentSize();
  }

  /**
   * Test getCurrentSize method given a Trip with User information.
   */
  @Test
  public void testGetCurrentSize() {
    List<String> memberIds = new LinkedList<String>();
    List<String> pendingIds = new LinkedList<String>();
    Trip hasUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(0, 1, 2, 3)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds);
    assertEquals(hasUsers.getCurrentSize(), 1);
    memberIds.add("1");
    assertEquals(hasUsers.getCurrentSize(), 2);
    pendingIds.add("2");
    assertEquals(hasUsers.getCurrentSize(), 2);
  }

  /**
   * Test getCostPerUser method given a Trip without User information.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetCostPerUserNull() {
    Trip noUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(5, 6, 7, 8)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 20, "1234567890", "My car", "comments").build();
    noUsers.getCostPerUser("userId");
  }

  /**
   * Test getCostPerUser method given a Trip with User information and a User
   * that exists in the Trip.
   */
  @Test
  public void testGetCostPerUser() {
    List<String> memberIds = new LinkedList<String>();
    List<String> pendingIds = new LinkedList<String>();
    Trip hasUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(0, 1, 2, 3)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds);
    assertEquals(hasUsers.getCostPerUser("host"), 100.0, 0.1);
    memberIds.add("1");
    assertEquals(hasUsers.getCostPerUser("1"), 50.0, 0.1);
    memberIds.add("2");
    memberIds.add("3");
    assertEquals(hasUsers.getCostPerUser("2"), 25.0, 0.1);
    pendingIds.add("4");
    assertEquals(hasUsers.getCostPerUser("3"), 25.0, 0.1);
  }

  /**
   * Test getCostPerUser method given a Trip with User information and a User
   * that does not exist in the Trip.
   */
  @Test
  public void testGetCostPerUserNotInTrip() {
    List<String> memberIds = new LinkedList<String>();
    List<String> pendingIds = new LinkedList<String>();
    Trip hasUsers = Trip.TripBuilder.newTripBuilder()
        .addIdentification(0, "name").addLocations(0, 1, 2, 3)
        .addAddressNames("start", "end").addTimes(10, 20)
        .addDetails(5, 100, "1234567890", "My car", "comments")
        .buildWithUsers("host", memberIds, pendingIds);
    assertEquals(hasUsers.getCostPerUser("stranger"), 50.0, 0.1);
    memberIds.add("1");
    memberIds.add("2");
    assertEquals(hasUsers.getCostPerUser("stranger"), 25.0, 0.1);
    memberIds.add("3");
    assertEquals(hasUsers.getCostPerUser("stranger"), 20.0, 0.1);
    pendingIds.add("4");
    assertEquals(hasUsers.getCostPerUser("stranger"), 20.0, 0.1);
  }

  /**
   * Test distanceTo method.
   */
  @Test
  public void testDistanceTo() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(1, 1, 3, 4).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(trip1.distanceTo(trip2), 557.97, 0.1);
  }

  /**
   * Test getTripDistance method.
   */
  @Test
  public void testGetTripDistance() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 3, 4).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(trip1.getTripDistance(), 0, 0.1);
    assertEquals(trip2.getTripDistance(), 555.81, 0.1);
  }

  /**
   * Test equals method.
   */
  @Test
  public void testEquals() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 3, 4).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(trip1, trip2);
    assertNotEquals(trip1, trip3);
    assertNotEquals(trip2, trip3);
  }

  /**
   * Test hashCode method.
   */
  @Test
  public void testHashCode() {
    Trip trip1 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    Trip trip2 = Trip.TripBuilder.newTripBuilder().addIdentification(1, "name1")
        .addLocations(0, 0, 0, 0).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    Trip trip3 = Trip.TripBuilder.newTripBuilder().addIdentification(2, "name2")
        .addLocations(0, 0, 3, 4).addAddressNames("start", "end")
        .addTimes(10, 20).addDetails(5, 20, "1234567890", "My car", "comments")
        .build();
    assertEquals(trip1.hashCode(), trip2.hashCode());
    assertNotEquals(trip1.hashCode(), trip3.hashCode());
    assertNotEquals(trip2.hashCode(), trip3.hashCode());
  }
}
