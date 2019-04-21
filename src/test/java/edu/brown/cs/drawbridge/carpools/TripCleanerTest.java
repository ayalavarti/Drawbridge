package edu.brown.cs.drawbridge.carpools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

/**
 * Test TripCleaner constructor and methods.
 */
public class TripCleanerTest {

  private static Carpools carpools;
  private static final User DUMMY_USER = new User("1", "one", "one@mail.com");
  private static final Trip DUMMY_TRIP = Trip.TripBuilder.newTripBuilder()
      .addIdentification(-1, "First trip").addLocations(3, 3, 5, 5)
      .addAddressNames("(3, 3)", "(5, 5)").addTimes(1000, 1500)
      .addDetails(5, 20.00, "555-555-5555", "car", "").build();
  private static int tripId;

  @BeforeClass
  public static void oneTimeSetUp()
      throws SQLException, MissingDataException, ClassNotFoundException {
    String username = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");
    /*
     * Run the following queries in pgadmin: CREATE USER <username> WITH
     * PASSWORD '<password>' GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public
     * TO <username> GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO
     * <username>
     */
    carpools = new Carpools("//127.0.0.1:5432/carpools", username, password);
  }

  @Test
  public void testRun()
      throws InterruptedException, SQLException, MissingDataException {
    carpools.addUser(DUMMY_USER);
    tripId = carpools.createTrip(DUMMY_TRIP, "1");
    assertEquals(carpools.getTrips("1").get(0).get(0).getName(),
        DUMMY_TRIP.getName());

    Timer t = new Timer();
    TripCleaner tripCleaner = new TripCleaner(carpools);
    t.scheduleAtFixedRate(tripCleaner, 0, 30);
    TimeUnit.SECONDS.sleep((long) 0.06);
    t.cancel();

    carpools.getTrips("1");
    assertTrue(carpools.getTrips("1").get(0).isEmpty());
    carpools.getDatabase().clearData();
  }
}
