package edu.brown.cs.drawbridge.database;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatabaseQueryTest {

  private static final User DUMMY_U1 = new User("1", "one", "one@mail.com");
  private static final User DUMMY_U2 = new User("2", "two", "two@mail.com");
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
  private static DatabaseQuery test;
  private static int t1, t2, t3;

  @BeforeClass
  public static void oneTimeSetUp()
      throws SQLException, MissingDataException, ClassNotFoundException {
    String username = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");
    /*
     * Run the following queries in pgadmin:
     * CREATE USER <username> WITH PASSWORD '<password>'
     * GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <username>
     * GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO <username>
     */
    test = new DatabaseQuery("jdbc:postgresql://127.0.0.1:5432/testCarpools",
        username, password);
    test.addUser(DUMMY_U1);
    test.addUser(DUMMY_U2);
    t1 = test.createTrip(DUMMY_T1, "1");
    t2 = test.createTrip(DUMMY_T2, "2");
    t3 = test.createTrip(DUMMY_T3, "2");
  }

  @AfterClass
  public static void oneTimeTearDown() throws SQLException {
    test.clearData();
  }

  @Test
  public void testAddUser() throws SQLException, MissingDataException {
    assertNotNull(test.getUserById("1"));
    assertNotNull(test.getUserById("2"));
  }

  @Test
  public void testCreateTrip() throws SQLException, MissingDataException {
    assertNotNull(test.getTripById(t1));
    assertNotNull(test.getTripById(t2));
    assertNotNull(test.getTripById(t3));
  }

  @Test public void getHostByTripId()
      throws SQLException, MissingDataException {
    assertEquals(test.getHostOnTrip(t1), "1");
    assertEquals(test.getHostOnTrip(t2), "2");
    assertEquals(test.getHostOnTrip(t3), "2");
  }

  @Test
  public void testDeleteTripManually()
      throws SQLException, MissingDataException {
    Trip t = Trip.TripBuilder.newTripBuilder().addIdentification(4, "extra")
        .addLocations(9, 9, 10, 12).addAddressNames("(9, 9)", "(10, 12)")
        .addTimes(0, 12).addDetails(4, 5.40, "-", "none", "").build();
    int tx = test.createTrip(t, "1");
    assertEquals(test.getHostTripsWithUser("1").size(), 2);
    assertTrue(test.getHostTripsWithUser("1").contains(tx));
    test.deleteTripManually(tx);
    assertEquals(test.getHostTripsWithUser("1").size(), 1);
    assertTrue(test.getHostTripsWithUser("1").contains(t1));
  }

  @Test
  public void testDeleteTripByTime()
      throws SQLException, MissingDataException {
    //need to test with a trip after current time
    test.deleteExpiredTrips();
    try {
      assertNull(test.getTripById(t1));
    } catch (MissingDataException e) {
      assert true;
    }
    try {
      assertNull(test.getTripById(t2));
    } catch (MissingDataException e) {
      assert true;
    }
    try {
      assertNull(test.getTripById(t3));
    } catch (MissingDataException e) {
      assert true;
    }
    int t4 = test.createTrip(
        Trip.TripBuilder.newTripBuilder().addIdentification(-1, "Late")
            .addLocations(10, 10, 15, 15)
            .addAddressNames("(10, 10)", "(15, 15)")
            .addTimes(1556470997, 1556479997)
            .addDetails(5, 30.00, "108-851-fake", "uber", "").build(),
        DUMMY_U1.getId());
    test.deleteExpiredTrips();
    assertNotNull(test.getTripById(t4));
    test.deleteTripManually(t4);
    try {
      assertNull(test.getTripById(t4));
    } catch (MissingDataException e) {
      assert true;
    }
    t1 = test.createTrip(DUMMY_T1, "1");
    t2 = test.createTrip(DUMMY_T2, "2");
    t3 = test.createTrip(DUMMY_T3, "2");
    assertNotNull(test.getTripById(t1));
    assertNotNull(test.getTripById(t2));
    assertNotNull(test.getTripById(t3));
  }

  @Test
  public void testGetRelevantTrips()
      throws SQLException, MissingDataException {
    assertEquals(test.getConnectedTripsWithinTimeRadius(3, 3, 0, 1000, 0),
        new ArrayList<>(Collections.singletonList(test.getTripById(t1))));
    //location but not time
    assertTrue(
        test.getConnectedTripsWithinTimeRadius(3, 3, 0, 2000, 0).isEmpty());
    //time but not location
    assertTrue(
        test.getConnectedTripsWithinTimeRadius(5, 5, 0, 1000, 0).isEmpty());
    //all trips
    assertEquals(test.getConnectedTripsWithinTimeRadius(1, 1, 330, 1500, 1250),
        new ArrayList<>(Arrays
            .asList(test.getTripById(t1), test.getTripById(t2),
                test.getTripById(t3))));
    //selective
    assertEquals(test.getConnectedTripsWithinTimeRadius(3, 3, 112, 750, 300),
        new ArrayList<>(
            Arrays.asList(test.getTripById(t1), test.getTripById(t3))));
  }

  @Test
  public void testGetConnectedTrips()
      throws SQLException, MissingDataException {
    //not location
    assertTrue(test.getConnectedTripsAfterEta(5, 5, 0, 1000, 0).isEmpty());
    //location but not time
    assertTrue(test.getConnectedTripsAfterEta(3, 3, 0, 2000, 0).isEmpty());
    //before eta within buffer
    assertTrue(test.getConnectedTripsAfterEta(3, 3, 0, 1021, 25).isEmpty());
    //after eta within buffer
    assertEquals(test.getConnectedTripsAfterEta(3, 3, 0, 975, 50),
        new ArrayList<>(Collections.singletonList(test.getTripById(t1))));
    //after eta outside buffer
    assertTrue(test.getConnectedTripsAfterEta(3, 3, 0, 1026, 25).isEmpty());
    //all trips
    assertEquals(
        test.getConnectedTripsAfterEta(1, 1, 330, -100000000, 2000000000),
        new ArrayList<>(Arrays
            .asList(test.getTripById(t1), test.getTripById(t2),
                test.getTripById(t3))));
    //selective
    assertEquals(test.getConnectedTripsAfterEta(1, 1, 158, -112320, 1000000),
        new ArrayList<>(Collections.singletonList(test.getTripById(t2))));
  }

  @Test
  public void testRequest() throws SQLException {
    test.request(t1, "1");
    test.request(t1, "2");
    assertTrue(test.getRequestsOnTrip(t1).contains("1"));
    assertTrue(test.getRequestsOnTrip(t1).contains("2"));
    assertTrue(test.getRequestTripsWithUser("1").contains(t1));
    assertTrue(test.getRequestTripsWithUser("2").contains(t1));
  }

  @Test
  public void testApprove() throws SQLException {
    test.request(t3, "1");
    test.request(t3, "2");
    assertTrue(test.getRequestsOnTrip(t3).contains("1"));
    assertTrue(test.getRequestsOnTrip(t3).contains("2"));
    test.approve(t3, "1");
    test.approve(t3, "2");
    assertTrue(test.getMembersOnTrip(t3).contains("1"));
    assertTrue(test.getMembersOnTrip(t3).contains("2"));
    assertTrue(test.getRequestsOnTrip(t3).isEmpty());
  }

  @Test
  public void testReject() throws SQLException {
    test.request(t2, "1");
    test.request(t2, "2");
    assertTrue(test.getRequestsOnTrip(t2).contains("1"));
    assertTrue(test.getRequestsOnTrip(t2).contains("2"));
    test.reject(t2, "1");
    test.reject(t2, "2");
    assertTrue(test.getMembersOnTrip(t2).isEmpty());
    assertTrue(test.getRequestsOnTrip(t2).isEmpty());
  }

  @Test
  public void testKick() throws SQLException {
    test.request(t1, "1");
    test.request(t1, "2");
    test.approve(t1, "1");
    test.approve(t1, "2");
    assertTrue(test.getMembersOnTrip(t1).contains("1"));
    assertTrue(test.getMembersOnTrip(t1).contains("2"));
    test.kick(t1, "1");
    assertTrue(test.getMembersOnTrip(t1).contains("2"));
    test.kick(t1, "2");
    assertTrue(test.getMembersOnTrip(t1).isEmpty());
  }

  @Test
  public void testUpdate() throws SQLException, MissingDataException {
    Trip trip1 = test.getTripById(t1);
    assertEquals(trip1.getComments(), "");
    test.updateTripDescription(t1, "This is a new comment");
    Trip trip1Changed = test.getTripById(t1);
    assertEquals(trip1Changed.getComments(), "This is a new comment");
  }
}
