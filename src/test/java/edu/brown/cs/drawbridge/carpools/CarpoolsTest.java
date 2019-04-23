package edu.brown.cs.drawbridge.carpools;

import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CarpoolsTest {

  private static final User DUMMY_U1 = new User("1", "one", "one@mail.com");
  private static final User DUMMY_U2 = new User("2", "two", "two@mail.com");
  private static final User DUMMY_U3 = new User("3", "three", "three@mail.com");
  private static final Trip DUMMY_T1 = Trip.TripBuilder.newTripBuilder()
      .addIdentification(-1, "First trip").addLocations(3, 3, 5, 5)
      .addAddressNames("(3, 3)", "(5, 5)").addTimes(1000, 1500)
      .addDetails(5, 20.00, "555-555-5555", "car", "").build();
  private static final Trip DUMMY_T2 = Trip.TripBuilder.newTripBuilder()
      .addIdentification(-1, "Second trip").addLocations(0, 0, 5, 4)
      .addAddressNames("(0, 0)", "(5, 4)").addTimes(900, 2000)
      .addDetails(4, 15.50, "444-444-4444", "car", "").build();
  private static final Trip DUMMY_T3 = Trip.TripBuilder.newTripBuilder()
      .addIdentification(-1, "Third trip").addLocations(2, 3, 9, 9)
      .addAddressNames("(2, 3)", "(9, 9)").addTimes(500, 1500)
      .addDetails(3, 16.00, "333-333-3333", "car", "").build();
  private static int t1, t2, t3; // Ids of new Trips
  private static Carpools carpools;

  @BeforeClass public static void oneTimeSetUp()
      throws SQLException, MissingDataException, ClassNotFoundException {
    try {
      String username = System.getenv("DB_USER");
      String password = System.getenv("DB_PASS");
      /*
       * Run the following queries in pgadmin: CREATE USER <username> WITH
       * PASSWORD '<password>' GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA
       * public TO <username> GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA
       * public TO <username>
       */
      carpools = new Carpools("jdbc:postgresql://127.0.0.1:5432/testCarpools",
          username, password);
    } catch (ClassNotFoundException | SQLException e) {
      assert false;
    }
    // Test add User
    assertNotNull(carpools.addUser(DUMMY_U1));
    assertNotNull(carpools.addUser(DUMMY_U2));
    assertNotNull(carpools.addUser(DUMMY_U3));
    // Test create Trip
    t1 = carpools.createTrip(DUMMY_T1, "1");
    t2 = carpools.createTrip(DUMMY_T2, "2");
    t3 = carpools.createTrip(DUMMY_T3, "2");
    assertNotNull(t1);
    assertNotNull(t2);
    assertNotNull(t3);
  }

  @AfterClass public static void testGetDatabase()
      throws SQLException, ClassNotFoundException {
    carpools.getDatabase().clearData();
  }

  @Test public void testAddUserGivenAlreadyExists()
      throws SQLException, MissingDataException {
    assertNotNull(carpools.addUser(DUMMY_U1));
    assertNotNull(carpools.addUser(DUMMY_U2));
  }

  @Test public void testGetTrip() throws SQLException, MissingDataException {
    assertNotNull(carpools.getTrip(t1));
    assertNotNull(carpools.getTrip(t2));
    assertNotNull(carpools.getTrip(t3));
  }

  @Test(expected = MissingDataException.class) public void testGetTripInvalid()
      throws SQLException, MissingDataException {
    assertEquals(carpools.getTrip(-10), -1);
  }

  @Test public void testDeleteTrip() throws SQLException, MissingDataException {
    assertTrue(carpools.deleteTrip(t1, "1"));
    try {
      carpools.getTrip(t1);
    } catch (MissingDataException e) {
      t1 = carpools.createTrip(DUMMY_T1, "1");
      return;
    }
    assert false;
  }

  @Test public void testJoinAndLeaveTrip()
      throws SQLException, MissingDataException {
    assertTrue(carpools.joinTrip(t1, "3"));
    assertFalse(carpools.joinTrip(t1, "1"));
    assertTrue(carpools.leaveTrip(t1, "3"));
    assertFalse(carpools.leaveTrip(t1, "1"));
  }

  @Test public void testApproveRequest()
      throws SQLException, MissingDataException {
    assertTrue(carpools.joinTrip(t1, "3"));
    assertTrue(carpools.approveRequest(t1, "1", "3"));
    assertFalse(carpools.approveRequest(t1, "3", "3"));
    assertFalse(carpools.approveRequest(t1, "1", "3"));
    assertTrue(carpools.leaveTrip(t1, "3"));
  }

  @Test public void testRejectRequest()
      throws SQLException, MissingDataException {
    assertTrue(carpools.joinTrip(t1, "3"));
    assertTrue(carpools.rejectRequest(t1, "1", "3"));
    assertFalse(carpools.approveRequest(t1, "3", "3"));
    assertFalse(carpools.approveRequest(t1, "1", "3"));
    assertFalse(carpools.leaveTrip(t1, "3"));
  }

  @Test public void testGetUsers() throws SQLException, MissingDataException {
    assertTrue(carpools.joinTrip(t1, "2"));
    assertTrue(carpools.joinTrip(t1, "3"));
    assertTrue(carpools.approveRequest(t1, "1", "2"));
    List<List<User>> expected = new ArrayList<>();
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_U1)));
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_U2)));
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_U3)));
    List<List<User>> actual = carpools.getUsers(t1);
    for (int i = 0; i < expected.size(); i++) {
      for (int j = 0; j < expected.get(i).size(); j++) {
        assertEquals(expected.get(i).get(j), actual.get(i).get(j));
      }
      assertEquals(expected.get(i).size(), actual.get(i).size());
    }
    assertEquals(expected.size(), actual.size());
    assertTrue(carpools.leaveTrip(t1, "2"));
    assertTrue(carpools.rejectRequest(t1, "1", "3"));
  }

  @Test public void testGetTrips() throws SQLException, MissingDataException {
    assertTrue(carpools.joinTrip(t2, "1"));
    assertTrue(carpools.joinTrip(t3, "1"));
    assertTrue(carpools.approveRequest(t2, "2", "1"));
    List<List<Trip>> expected = new ArrayList<>();
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_T1)));
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_T2)));
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_T3)));
    List<List<Trip>> actual = carpools.getTrips("1");
    for (int i = 0; i < expected.size(); i++) {
      for (int j = 0; j < expected.get(i).size(); j++) {
        assertEquals(expected.get(i).get(j).getName(),
            actual.get(i).get(j).getName());
      }
    }
    assertTrue(carpools.leaveTrip(t2, "1"));
    assertTrue(carpools.leaveTrip(t3, "1"));
  }

  @Test public void testSearch() throws SQLException, MissingDataException {
    List<List<Trip>> expected = new ArrayList<>();
    expected.add(new ArrayList<>(Arrays.asList(DUMMY_T1)));
    List<List<Trip>> actual = carpools
        .searchWithoutId(3, 3, 5, 5, 1000, 15, 30);
    for (int i = 0; i < expected.size(); i++) {
      for (int j = 0; j < expected.get(i).size(); j++) {
        assertEquals(expected.get(i).get(j).getName(),
            actual.get(i).get(j).getName());
      }
    }

    actual = carpools.searchWithId("1", 3, 3, 5, 5, 1000, 15, 30);
    for (int i = 0; i < expected.size(); i++) {
      for (int j = 0; j < expected.get(i).size(); j++) {
        assertEquals(expected.get(i).get(j).getName(),
            actual.get(i).get(j).getName());
      }
    }
  }
}
