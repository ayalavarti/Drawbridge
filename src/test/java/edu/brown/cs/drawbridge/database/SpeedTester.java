package edu.brown.cs.drawbridge.database;

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

  @BeforeClass
  public static void oneTimeSetUp() throws SQLException, ClassNotFoundException {
    String username = "dev";
    String password = "dev";
    /*
     * Run the following queries in pgadmin:
     * CREATE USER <username> WITH PASSWORD '<password>'
     * GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <username>
     * GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO <username>
     */
    test = new DatabaseQuery("//127.0.0.1:5432/massData", username,
            password);
  }

  private static Long lastTime;

  private void timeSince() {
    if (lastTime != null) {
      System.out.println("elapsed: " + (System.currentTimeMillis() - lastTime));
    }
    lastTime = System.currentTimeMillis();
  }

  @Test
  public void testGetUser() throws MissingDataException, SQLException {
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
  }

  @Test
  public void testGetTrip() throws MissingDataException, SQLException {
    timeSince();
    Trip t = test.getTripById(50);
    timeSince();
    assertEquals(t.getName(), "zlgflxily");
    assertEquals(t.getMemberIds(), Collections.singletonList("50"));
  }

  @Test
  public void testSearchTrips() throws MissingDataException, SQLException {
    timeSince();
    List<Trip> t1 = test.getConnectedTripsWithinTimeRadius(
            88.35, 127.74, 10, 1555870749, 3600);
    timeSince();
    assertFalse(t1.isEmpty());
    List<Trip> t2 = test.getConnectedTripsAfterEta(
            88.35, 127.74, 10, 1555860000, 36000);
    timeSince();
    assertFalse(t2.isEmpty());
  }

}
