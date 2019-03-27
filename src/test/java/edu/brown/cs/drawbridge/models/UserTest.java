package edu.brown.cs.drawbridge.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * Tests the User constructor and methods.
 */
public class UserTest {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(new User("id", "name", "email"));
  }

  /**
   * Test getId method.
   */
  @Test
  public void testGetId() {
    User u1 = new User("id1", "name", "email");
    User u2 = new User("id2", "name", "email");
    assertEquals(u1.getId(), "id1");
    assertEquals(u2.getId(), "id2");
  }

  /**
   * Test getName method.
   */
  @Test
  public void testGetName() {
    User u1 = new User("id1", "name1", "email");
    User u2 = new User("id2", "name2", "email");
    assertEquals(u1.getName(), "name1");
    assertEquals(u2.getName(), "name2");
  }

  /**
   * Test getEmail method.
   */
  @Test
  public void testGetEmail() {
    User u1 = new User("id1", "name1", "email1");
    User u2 = new User("id2", "name2", "email2");
    assertEquals(u1.getEmail(), "email1");
    assertEquals(u2.getEmail(), "email2");
  }

  /**
   * Test getHostingTrips method.
   */
  @Test
  public void testGetHostingTrips() {
    List<Integer> hosting = new LinkedList<Integer>();
    hosting.add(1);
    hosting.add(2);
    List<Integer> member = new LinkedList<Integer>();
    member.add(3);
    member.add(4);
    List<Integer> pending = new LinkedList<Integer>();
    pending.add(5);
    pending.add(6);
    User u = new User("id1", "name1", "email");
    u.setTrips(hosting, member, pending);
    for (int i = 0; i < hosting.size(); i++) {
      assertEquals(u.getHostingTrips().get(i), hosting.get(i));
    }
  }

  /**
   * Test getMemberTrips method.
   */
  @Test
  public void testGetMemberTrips() {
    List<Integer> hosting = new LinkedList<Integer>();
    hosting.add(1);
    hosting.add(2);
    List<Integer> member = new LinkedList<Integer>();
    member.add(3);
    member.add(4);
    List<Integer> pending = new LinkedList<Integer>();
    pending.add(5);
    pending.add(6);
    User u = new User("id1", "name1", "email");
    u.setTrips(hosting, member, pending);
    for (int i = 0; i < member.size(); i++) {
      assertEquals(u.getMemberTrips().get(i), member.get(i));
    }
  }

  /**
   * Test getPendingTrips method.
   */
  @Test
  public void testGetPendingTrips() {
    List<Integer> hosting = new LinkedList<Integer>();
    hosting.add(1);
    hosting.add(2);
    List<Integer> member = new LinkedList<Integer>();
    member.add(3);
    member.add(4);
    List<Integer> pending = new LinkedList<Integer>();
    pending.add(5);
    pending.add(6);
    User u = new User("id1", "name1", "email");
    u.setTrips(hosting, member, pending);
    for (int i = 0; i < pending.size(); i++) {
      assertEquals(u.getPendingTrips().get(i), pending.get(i));
    }
  }

  /**
   * Test setUsers method.
   */
  @Test
  public void testSetUsers() {
    List<Integer> hosting = new LinkedList<Integer>();
    hosting.add(1);
    hosting.add(2);
    List<Integer> member = new LinkedList<Integer>();
    member.add(3);
    member.add(4);
    List<Integer> pending = new LinkedList<Integer>();
    pending.add(5);
    pending.add(6);
    User u = new User("id1", "name1", "email1");
    u.setTrips(hosting, member, pending);
    for (int i = 0; i < hosting.size(); i++) {
      assertEquals(u.getHostingTrips().get(i), hosting.get(i));
    }
    for (int i = 0; i < member.size(); i++) {
      assertEquals(u.getMemberTrips().get(i), member.get(i));
    }
    for (int i = 0; i < pending.size(); i++) {
      assertEquals(u.getPendingTrips().get(i), pending.get(i));
    }
  }

  /**
   * Test getHostingTrips method given no Trip data.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetHostingTripsNoTrips() {
    User u = new User("id1", "name1", "email");
    u.getMemberTrips();
  }

  /**
   * Test getMemberTrips method given no Trip data.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetMemberTripsNoTrips() {
    User u = new User("id1", "name1", "email");
    u.getMemberTrips();
  }

  /**
   * Test getPendingTrips method given no Trip data.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetPendingTripsNoTrip() {
    User u = new User("id1", "name1", "email");
    u.getPendingTrips();
  }
}
