package edu.brown.cs.drawbridge.database;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class DatabaseQueryTest {

  private static DatabaseQuery test;

  private static final User DUMMY_U1 = new User("1", "one", "one@mail.com");
  private static final User DUMMY_U2 = new User("2", "two", "two@mail.com");
  private static final Trip DUMMY_T1 = Trip.TripBuilder.newTripBuilder()
          .addIdentification(1, "First trip")
          .addLocations(3, 3, 5, 5)
          .addAddressNames("(3, 3)", "(5, 5)")
          .addTimes(1000, 1500)
          .addDetails(5, 20.00, "555-555-5555", "car", "")
          .build();
  private static final Trip DUMMY_T2 = Trip.TripBuilder.newTripBuilder()
          .addIdentification(2, "Second trip")
          .addLocations(0, 0, 5, 4)
          .addAddressNames("(0, 0)", "(5, 4)")
          .addTimes(900, 2000)
          .addDetails(4, 15.50, "444-444-4444", "car", "")
          .build();
  private static final Trip DUMMY_T3 = Trip.TripBuilder.newTripBuilder()
          .addIdentification(3, "Third trip")
          .addLocations(2, 3, 9, 9)
          .addAddressNames("(2, 3)", "(9, 9)")
          .addTimes(500, 1500)
          .addDetails(3, 16.00, "333-333-3333", "car", "")
          .build();
  private static int t1, t2, t3;

  @BeforeClass
  public static void oneTimeSetUp() {
    try {
      String username = "dev";
      String password = "dev";
      /*
       * If your database denies access, run the following queries in pgadmin:
       * CREATE USER dev WITH PASSWORD 'dev'; GRANT ALL PRIVILEGES ON ALL TABLES
       * IN SCHEMA public TO dev;
       */
      test = new DatabaseQuery("//127.0.0.1:5432/carpools", username, password);
      test.setUp();
      test.addUser(DUMMY_U1);
      test.addUser(DUMMY_U2);
      t1 = test.createTrip(DUMMY_T1, "1");
      t2 = test.createTrip(DUMMY_T2, "2");
      t3 = test.createTrip(DUMMY_T3, "2");
    } catch (ClassNotFoundException | SQLException e) {
      assert false;
    }
  }

  @Test
  public void testAddUser() {
    test.addUser(DUMMY_U1);
    test.addUser(DUMMY_U2);
    assertNotNull(test.getUserById("1"));
    assertNotNull(test.getUserById("2"));
  }

  @Test
  public void testCreateTrip() {
    assertNotNull(test.getTripById(t1));
    assertNotNull(test.getTripById(t2));
    assertNotNull(test.getTripById(t3));
  }

  @Test
  public void getHostByTripId() {
    assertEquals(test.getHostOnTrip(t1), "1");
    assertEquals(test.getHostOnTrip(t2), "2");
    assertEquals(test.getHostOnTrip(t3), "2");
  }

  @Test
  public void testDeleteTripManually() {
    Trip t = Trip.TripBuilder.newTripBuilder()
            .addIdentification(4, "extra")
            .addLocations(9, 9, 10, 12)
            .addAddressNames("(9, 9)", "(10, 12)")
            .addTimes(0, 12)
            .addDetails(4, 5.40, "-","none", "")
            .build();
    int tx = test.createTrip(t, "1");
    assertEquals(test.getHostTripsWithUser("1").size(), 2);
    assertTrue(test.getHostTripsWithUser("1").contains(tx));
    assertTrue(test.deleteTripManually(tx));
    assertEquals(test.getHostTripsWithUser("1").size(), 1);
    assertTrue(test.getHostTripsWithUser("1").contains(t1));
  }

  @Test
  public void testDeleteTripByTime() {
    assertTrue(test.deleteExpiredTrips());
    assertNull(test.getTripById(t1));
    assertNull(test.getTripById(t2));
    assertNull(test.getTripById(t3));
    t1 = test.createTrip(DUMMY_T1, "1");
    t2 = test.createTrip(DUMMY_T2, "2");
    t3 = test.createTrip(DUMMY_T3, "2");
    assertNotNull(test.getTripById(t1));
    assertNotNull(test.getTripById(t2));
    assertNotNull(test.getTripById(t3));
  }

  @Test
  public void testGetRelevantTrips() {

  }

  @Test
  public void testGetConnectedTrips() {

  }

  @Test
  public void testRequest() {
    assertTrue(test.request(t1, "1"));
    assertTrue(test.request(t1, "2"));
    assertTrue(test.getRequestsOnTrip(t1).contains("1"));
    assertTrue(test.getRequestsOnTrip(t1).contains("2"));
    assertTrue(test.getRequestTripsWithUser("1").contains(t1));
    assertTrue(test.getRequestTripsWithUser("2").contains(t1));
  }

  @Test
  public void testApprove() {
    assertTrue(test.request(t3, "1"));
    assertTrue(test.request(t3, "2"));
    assertTrue(test.getRequestsOnTrip(t3).contains("1"));
    assertTrue(test.getRequestsOnTrip(t3).contains("2"));
    assertTrue(test.approve(t3, "1"));
    assertTrue(test.approve(t3, "2"));
    assertTrue(test.getMembersOnTrip(t3).contains("1"));
    assertTrue(test.getMembersOnTrip(t3).contains("2"));
    assertTrue(test.getRequestsOnTrip(t3).isEmpty());
  }

  @Test
  public void testReject() {
    assertTrue(test.request(t2, "1"));
    assertTrue(test.request(t2, "2"));
    assertTrue(test.getRequestsOnTrip(t2).contains("1"));
    assertTrue(test.getRequestsOnTrip(t2).contains("2"));
    assertTrue(test.reject(t2, "1"));
    assertTrue(test.reject(t2, "2"));
    assertTrue(test.getMembersOnTrip(t2).isEmpty());
    assertTrue(test.getRequestsOnTrip(t2).isEmpty());
  }

  @Test
  public void testKick() {
    assertTrue(test.request(t1, "1"));
    assertTrue(test.request(t1, "2"));
    assertTrue(test.approve(t1, "1"));
    assertTrue(test.approve(t1, "2"));
    assertTrue(test.getMembersOnTrip(t1).contains("1"));
    assertTrue(test.getMembersOnTrip(t1).contains("2"));
    assertTrue(test.kick(t1, "1"));
    assertTrue(test.getMembersOnTrip(t1).contains("2"));
    assertTrue(test.kick(t1, "2"));
    assertTrue(test.getMembersOnTrip(t1).isEmpty());
  }

  @AfterClass
  public static void oneTimeTearDown() {
    test.clearData();
  }
}
