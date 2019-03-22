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
    assertNotNull(new User("id", "name", new LinkedList<String>(),
        new LinkedList<String>(), new LinkedList<String>()));
  }

  /**
   * Test getId method.
   */
  @Test
  public void testGetId() {
    List<String> hosting = new LinkedList<String>();
    hosting.add("1");
    List<String> member = new LinkedList<String>();
    member.add("2");
    List<String> pending = new LinkedList<String>();
    pending.add("3");
    User u1 = new User("id1", "name", hosting, member, pending);
    User u2 = new User("id2", "name", hosting, member, pending);
    assertEquals(u1.getId(), "id1");
    assertEquals(u2.getId(), "id2");
  }

  /**
   * Test getName method.
   */
  @Test
  public void testGetName() {
    List<String> hosting = new LinkedList<String>();
    hosting.add("1");
    List<String> member = new LinkedList<String>();
    member.add("2");
    List<String> pending = new LinkedList<String>();
    pending.add("3");
    User u1 = new User("id1", "name1", hosting, member, pending);
    User u2 = new User("id2", "name2", hosting, member, pending);
    assertEquals(u1.getName(), "name1");
    assertEquals(u2.getName(), "name2");
  }

  /**
   * Test getHostingTrips method.
   */
  @Test
  public void testGetHostingTrips() {
    List<String> hosting = new LinkedList<String>();
    hosting.add("1");
    hosting.add("2");
    List<String> member = new LinkedList<String>();
    member.add("3");
    member.add("4");
    List<String> pending = new LinkedList<String>();
    pending.add("5");
    pending.add("6");
    User u = new User("id1", "name1", hosting, member, pending);
    for (int i = 0; i < hosting.size(); i++) {
      assertEquals(u.getHostingTrips().get(i), hosting.get(i));
    }
  }

  /**
   * Test getMemberTrips method.
   */
  @Test
  public void testGetMemberTrips() {
    List<String> hosting = new LinkedList<String>();
    hosting.add("1");
    hosting.add("2");
    List<String> member = new LinkedList<String>();
    member.add("3");
    member.add("4");
    List<String> pending = new LinkedList<String>();
    pending.add("5");
    pending.add("6");
    User u = new User("id1", "name1", hosting, member, pending);
    for (int i = 0; i < member.size(); i++) {
      assertEquals(u.getMemberTrips().get(i), member.get(i));
    }
  }

  /**
   * Test getPendingTrips method.
   */
  @Test
  public void testGetPendingTrips() {
    List<String> hosting = new LinkedList<String>();
    hosting.add("1");
    hosting.add("2");
    List<String> member = new LinkedList<String>();
    member.add("3");
    member.add("4");
    List<String> pending = new LinkedList<String>();
    pending.add("5");
    pending.add("6");
    User u = new User("id1", "name1", hosting, member, pending);
    for (int i = 0; i < pending.size(); i++) {
      assertEquals(u.getPendingTrips().get(i), pending.get(i));
    }
  }
}
