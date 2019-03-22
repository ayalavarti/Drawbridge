package edu.brown.cs.drawbridge.models;

import static org.junit.Assert.assertEquals;
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
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertNotNull(t);
  }

  /**
   * Test getId method.
   */
  @Test
  public void testGetId() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getId(), 0);
  }

  /**
   * Test getName method.
   */
  @Test
  public void testGetName() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getName(), "name");
  }

  /**
   * Test getStartingLatitude method.
   */
  @Test
  public void testGetStartingLatitude() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.001, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getStartingLatitude(), 0.001, 0.1);
  }

  /**
   * Test getStartingLongitude method.
   */
  @Test
  public void testGetStartingLongitude() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.002, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getStartingLongitude(), 0.002, 0.1);
  }

  /**
   * Test getStartingLongitude method.
   */
  @Test
  public void testGetEndingLatitude() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.003, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getEndingLatitude(), 0.003, 0.1);
  }

  /**
   * Test getStartingLongitude method.
   */
  @Test
  public void testGetEndingLongitude() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.004, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getEndingLongitude(), 0.004, 0.1);
  }

  /**
   * Test getDepartureTime method.
   */
  @Test
  public void testGetDepartureTime() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getDepartureTime(), 10);
  }

  /**
   * Test getGetEta method.
   */
  @Test
  public void testGetEta() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getEta(), 20);
  }

  /**
   * Test getMaxUsers method.
   */
  @Test
  public void testGetMaxUsers() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getMaxUsers(), 5);
  }

  /**
   * Test getCost method.
   */
  @Test
  public void testGetCost() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getCost(), 100.0, 0.1);
  }

  /**
   * Test getPhoneNumber method.
   */
  @Test
  public void testGetPhoneNumber() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getPhoneNumber(), "1234567890");
  }

  /**
   * Test getMethodOfTransportation method.
   */
  @Test
  public void testGetMethodOfTransportation() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getMethodOfTransportation(), "My Car");
  }

  /**
   * Test getComments method.
   */
  @Test
  public void testGetComments() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getComments(), "comments");
  }

  /**
   * Test getHostId method.
   */
  @Test
  public void testGetHostId() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    assertEquals(t.getHostId(), "hostId");
  }

  /**
   * Test getMemberIds method.
   */
  @Test
  public void testGetMemberIds() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    for (int i = 0; i < memberIds.size(); i++) {
      assertEquals(t.getMemberIds().get(i), memberIds.get(i));
    }
  }

  /**
   * Test getPendingIds method.
   */
  @Test
  public void testGetPendingIds() {
    List<String> memberIds = new LinkedList<String>();
    memberIds.add("1");
    memberIds.add("2");
    List<String> pendingIds = new LinkedList<String>();
    pendingIds.add("3");
    pendingIds.add("4");
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();
    for (int i = 0; i < memberIds.size(); i++) {
      assertEquals(t.getPendingIds().get(i), pendingIds.get(i));
    }
  }

  /**
   * Test getCurrentSize method.
   */
  @Test
  public void testGetCurrentSize() {
    List<String> memberIds = new LinkedList<String>();
    List<String> pendingIds = new LinkedList<String>();
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();

    assertEquals(t.getCurrentSize(), 1);
    memberIds.add("1");
    assertEquals(t.getCurrentSize(), 2);
    pendingIds.add("2");
    assertEquals(t.getCurrentSize(), 2);
  }

  /**
   * Test getCostPerUser method.
   */
  @Test
  public void testGetCostPerUser() {
    List<String> memberIds = new LinkedList<String>();
    List<String> pendingIds = new LinkedList<String>();
    Trip t = new Trip.Builder(0, "name", 0.1, 0.2, 0.3, 0.4, 10, 20, 5, 100.0,
        "1234567890", "My Car", "comments", "hostId", memberIds, pendingIds)
            .build();

    assertEquals(t.getCostPerUser(), 100.0, 0.1);
    memberIds.add("1");
    assertEquals(t.getCostPerUser(), 50.0, 0.1);
    memberIds.add("2");
    memberIds.add("3");
    assertEquals(t.getCostPerUser(), 25.0, 0.1);
    pendingIds.add("4");
    assertEquals(t.getCostPerUser(), 25.0, 0.1);
  }
}
