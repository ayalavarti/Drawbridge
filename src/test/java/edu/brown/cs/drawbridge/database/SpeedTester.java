package edu.brown.cs.drawbridge.database;

import edu.brown.cs.drawbridge.carpools.Carpools;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SpeedTester {

  private static DatabaseQuery test;
  private static DatabaseQuery speed;

  @BeforeClass
  public static void oneTimeSetUp() throws SQLException, ClassNotFoundException {
    String username = "dev";//System.getenv("DB_USER");
    String password = "dev";//System.getenv("DB_PASS");
    /*
     * Run the following queries in pgadmin:
     * CREATE USER <username> WITH PASSWORD '<password>'
     * GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <username>
     * GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO <username>
     */
    test = new DatabaseQuery("//127.0.0.1:5432/massData", username,
            password);
    speed = new DatabaseQuery("//127.0.0.1:5432/speed", username, password);
  }

  private static Long lastTime;
  private static boolean midSize = false;
  private static boolean bigSize = false;

  private long timeSince() {
    long elapsed = 0;
    if (lastTime != null) {
      elapsed = System.currentTimeMillis() - lastTime;
      System.out.println("elapsed: " + elapsed);
    }
    lastTime = System.currentTimeMillis();
    return elapsed;
  }

  @Test
  public void testGetUser() throws MissingDataException, SQLException {
    if (midSize) {
      timeSince();
      User u1 = test.getUserById("0");
      timeSince();
      assertEquals(u1.getName(), "gdfokvyxa");
      User u2 = test.getUserById("999");
      timeSince();
      assertEquals(u2.getName(), "vogjkhc");
      User u3 = test.getUserById("487");
      timeSince();
      assertEquals(u3.getName(), "hiawfdbq");
    } else if (bigSize) {
      timeSince();
      User u1 = test.getUserById("383");
      assertTrue(timeSince() < 100);
      assertEquals(u1.getName(), "pxzqq");
    }
  }

  @Test
  public void testGetTrip() throws MissingDataException, SQLException {
    if (midSize) {
      timeSince();
      Trip t = test.getTripById(50);
      timeSince();
      assertEquals(t.getName(), "zlgflxily");
      assertEquals(t.getMemberIds(), Collections.singletonList("50"));
    } else if (bigSize) {
      timeSince();
      Trip t = test.getTripById(50);
      assertTrue(timeSince() < 1000);
      assertEquals(t.getName(), "wvqwym");
      assertEquals(t.getMemberIds(), Collections.singletonList("50"));
    }
  }

  @Test
  public void testSearchTrips() throws MissingDataException, SQLException, ClassNotFoundException {
    if (midSize) {
      timeSince();
      List<Trip> t1 = test.getConnectedTripsWithinTimeRadius(
              88.35, 127.74, 10, 1555870749, 3600);
      assertTrue(timeSince() < 100);
      assertFalse(t1.isEmpty());
      List<Trip> t2 = test.getConnectedTripsAfterEta(
              88.35, 127.74, 10, 1555860000, 36000);
      assertTrue(timeSince() < 100);
      assertFalse(t2.isEmpty());
      //walk distance = halfway around the equator
      List<Trip> t3 = test.getConnectedTripsWithinTimeRadius(
              0, 0, Math.PI * 6371, 1555870749, 100000);
      assertTrue(timeSince() < 10000);
      List<Trip> t4 = test.getConnectedTripsAfterEta(
              0, 0, Math.PI * 6371, 1555860000, 100000);
      assertTrue(timeSince() < 15000);
      System.out.println(t3.size());
      System.out.println(t4.size());
    } else if (bigSize) {
      timeSince();
      List<Trip> t1 = test.getConnectedTripsWithinTimeRadius(
              48.582, -93.682, 25, 1555887384, 3600);
      assertTrue(timeSince() < 5000);
      assertFalse(t1.isEmpty());
      List<Trip> t2 = test.getConnectedTripsAfterEta(
              72.035, -53.227, 25, 1556010000, 1000000);
      assertTrue(timeSince() < 5000);
      assertEquals(t2.size(), 2);
      //walk distance = halfway around the equator
//      List<Trip> t3 = test.getConnectedTripsWithinTimeRadius(
//              0, 0, Math.PI * 6371, 1555870749, 10000);
//      assertTrue(timeSince() < 60000);
//      List<Trip> t4 = test.getConnectedTripsAfterEta(
//              72.035, -53.227, Math.PI * 6371, 1556010000, 1000000);
//      assertFalse(t4.isEmpty());
//      assertTrue(timeSince() < 60000);
//      System.out.println(t3.size());
//      System.out.println(t4.size());
      Carpools cp = new Carpools("//127.0.0.1:5432/massData", "dev", "dev");
      timeSince();
      List<List<Trip>> results = cp.searchWithId("1", 54.8743144468108, -107.696585988682, 88.2212956652336, -172.076626546341, 1556081006,1440, 1440);
      System.out.println(results.size());
      for (List<Trip> connectedTrips : results) {
        for (Trip t : connectedTrips) {
          System.out.println(t.getName());
        }
      }
      assertTrue(timeSince() < 2000);
    }
  }

  @Test
  public void testModifications() throws MissingDataException, SQLException {
    if (midSize) {
      timeSince();
      test.addUser(new User("1001", "random", "not@real.com"));
      assertTrue(timeSince() < 100);

      int tx = test.createTrip(Trip.TripBuilder.newTripBuilder()
              .addIdentification(-1, "random")
              .addLocations(0,0,0,0)
              .addAddressNames("nowhere", "nowhere")
              .addTimes(0, 0)
              .addDetails(5, 5.00, "","","")
              .build(), "1");
      assertTrue(timeSince() < 100);

      test.request(tx, "1001");
      assertTrue(timeSince() < 100);

      test.approve(tx, "1001");
      assertTrue(timeSince() < 100);

      test.kick(tx, "1001");
      assertTrue(timeSince() < 100);

      test.deleteExpiredTrips();
      assertTrue(timeSince() < 5000);

      test.deleteUser("1001");
    } else if (bigSize) {
      timeSince();
      test.addUser(new User("1001", "random", "not@real.com"));
      assertTrue(timeSince() < 100);

      int tx = test.createTrip(Trip.TripBuilder.newTripBuilder()
              .addIdentification(-1, "random")
              .addLocations(0,0,0,0)
              .addAddressNames("nowhere", "nowhere")
              .addTimes(0, 0)
              .addDetails(5, 5.00, "","","")
              .build(), "1");
      assertTrue(timeSince() < 100);

      test.request(tx, "1001");
      assertTrue(timeSince() < 100);

      test.approve(tx, "1001");
      assertTrue(timeSince() < 100);

      test.kick(tx, "1001");
      assertTrue(timeSince() < 100);

      test.deleteExpiredTrips();
      assertTrue(timeSince() < 30000);

      test.deleteUser("1001");
    }
  }

  //@Test
  public void speedTest() throws SQLException, ClassNotFoundException, MissingDataException {
    Carpools cp = new Carpools("//127.0.0.1:5432/speed", "dev", "dev");
    timeSince();
    List<List<Trip>> results = cp.searchWithId(
            "1", 42.34819, -71.078705 , 39.953342, -75.15931, 1557154980, 1440, 1440);
    System.out.println(results.size());
    for (List<Trip> ts : results) {
      System.out.println(ts.size());
      System.out.println(ts.get(0).getName());
    }
    System.out.println(results.size());
    timeSince();
  }

}
