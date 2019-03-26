package edu.brown.cs.drawbridge.database;

import edu.brown.cs.drawbridge.models.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseQueryTest {

  private static DatabaseQuery test;

  @BeforeClass
  public static void oneTimeSetUp() {
    try {
      String username = "dev";
      String password = "dev";
      /*
      If your database denies access, run the following queries in pgadmin:
      CREATE USER dev WITH PASSWORD 'dev';
      GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dev;
       */
      test = new DatabaseQuery("//127.0.0.1:5432/carpools", username, password);
    } catch (ClassNotFoundException | SQLException e) {
      assert false;
    }
  }

  @Test
  public void testAddUser() {
    assertTrue(test.addUser(test.DUMMY_USER));
  }

  @Test
  public void testCreateTrip() {
    assertTrue(test.createTrip(test.DUMMY_TRIP, "0"));
  }

  @Test
  public void testGetUserById() {

  }

  @Test
  public void testGetTripById() {

  }

  @Test
  public void getHostByTripId() {

  }

  @Test
  public void getMembersByTripId() {

  }

  @Test
  public void getRequestsByTripId() {

  }

  @Test
  public void getTripsByHostId() {

  }

  @Test
  public void getTripsByMemberId() {

  }

  @Test
  public void getTripsByRequesterId() {

  }

  @Test
  public void testDeleteTripManually() {

  }

  @Test
  public void testDeleteTripByTime() {

  }

  @Test
  public void testGetRelevantTrips() {

  }

  @Test
  public void testGetConnectedTrips() {

  }

  @Test
  public void testRequest() {

  }

  @Test
  public void testApprove() {

  }

  @Test
  public void testReject() {

  }

  @Test
  public void testKick() {

  }
}
