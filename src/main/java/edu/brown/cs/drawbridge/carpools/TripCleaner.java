package edu.brown.cs.drawbridge.carpools;

import java.sql.SQLException;
import java.util.TimerTask;

import edu.brown.cs.drawbridge.database.DatabaseQuery;

/**
 * A TimerTask that clears expired trips (trips with departure time that has
 * passed).
 */
public class TripCleaner extends TimerTask {
  private DatabaseQuery database;

  /**
   * Create a new TripCleaner object.
   *
   * @param carpools
   *          A Carpools object containing the database to clear.
   */
  public TripCleaner(Carpools carpools) {
    database = carpools.getDatabase();
  }

  /**
   * Clear expired trips (trips with departure time that has passed).
   */
  @Override
  public void run() {
    try {
      database.deleteExpiredTrips();
    } catch (SQLException e) {
      System.out.println("ERROR: Could could not clear old trips.");
    }
  }
}
