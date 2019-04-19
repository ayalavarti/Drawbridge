package edu.brown.cs.drawbridge.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import retrofit2.http.Query;

/**
 * Contains a Test that populates the database.
 */
public class AutoPopulator {

  private static DatabaseQuery test;

  private static final int NUM_TRIPS = 10000;
  private static final int NUM_USERS = 1000;

  /**
   * Populate all tables in the database.
   *
   * @throws ClassNotFoundException
   *           When connection to database is invalid
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  //@Test
  public void populateUsers()
      throws ClassNotFoundException, SQLException, MissingDataException {
    String username = System.getenv("DB_USER");
    String password = System.getenv("DB_PASS");
    test = new DatabaseQuery("//127.0.0.1:5432/massData", username, password);
    populateUsers(test);
  }

  //@Test
  public void populateTrips() throws SQLException, ClassNotFoundException, MissingDataException {
    String username = "dev";
    String password = "dev";
    test = new DatabaseQuery("//127.0.0.1:5432/massData", username, password);
    populateTrips(test);
  }

  //@Test
  public void populateRequests() throws SQLException, ClassNotFoundException, MissingDataException {
    String username = "dev";
    String password = "dev";
    test = new DatabaseQuery("//127.0.0.1:5432/massData", username, password);
    populatePending(test);
  }

  //@Test
  public void populateMembers() throws SQLException, ClassNotFoundException, MissingDataException {
    String username = "dev";
    String password = "dev";
    test = new DatabaseQuery("//127.0.0.1:5432/massData", username, password);
    populateMembers(test);
  }

  private void populateUsers(DatabaseQuery db)
      throws SQLException, MissingDataException, ClassNotFoundException {
    for (int i = 0; i < NUM_USERS; i++) {
      User user = new User(Integer.toString(i), generateRandomString(),
          generateRandomString() + "@gmail.com");
      db.addUser(user);
    }
  }

  private void populateTrips(DatabaseQuery db)
      throws SQLException, MissingDataException, ClassNotFoundException {
    final int secondsPerHour = 3600;
    int hostId = 0;
    for (int i = 0; i < NUM_TRIPS; i++) {
      long departureTime = generateRandomTime();
      int maxUsers = (int) (Math.random() * 5) + 2;
      final int maxCost = 25;
      double cost = Math.random() * maxCost;
      Trip trip = Trip.TripBuilder.newTripBuilder()
          .addIdentification(-1, generateRandomString())
          .addLocations(generateRandomLat(), generateRandomLon(),
              generateRandomLat(), generateRandomLon())
          .addAddressNames(generateRandomString(), generateRandomString())
          .addTimes(departureTime, departureTime + secondsPerHour)
          .addDetails(maxUsers, cost, generateRandomPhoneNumber(),
              generateRandomTransportation(), generateRandomString())
          .build();
      db.createTrip(trip, Integer.toString(hostId));
      hostId = (hostId + 1) % NUM_USERS;
    }
  }

  private void populatePending(DatabaseQuery db)
      throws SQLException, MissingDataException, ClassNotFoundException {
    List<String> allUserIds = new ArrayList<String>();
    for (int i = 0; i < NUM_USERS; i++) {
      allUserIds.add(Integer.toString(i));
    }

    for (int i = 1; i <= NUM_TRIPS; i++) {
      Trip trip = db.getTripById(i);
      List<String> userIds = allUserIds.subList(0, allUserIds.size());
      userIds.remove(trip.getHostId());
      int maxRequests = trip.getMaxUsers() - 1;
      int numRequested = 0;
      for (String user : userIds) {
        // Make request for 75% of users
        final double chance = 0.75;
        if (Math.random() < chance && numRequested < maxRequests) {
          db.request(i, user);
          numRequested++;
        }
      }
    }
  }

  private void populateMembers(DatabaseQuery db)
      throws SQLException, MissingDataException, ClassNotFoundException {
    for (int i = 1; i <= NUM_TRIPS; i++) {
      List<String> userIds = db.getTripById(i).getPendingIds();
      int maxMembers = userIds.size();
      int numMembers = 0;
      for (String user : userIds) {
        // Make request for 75% of users
        final double chance = 0.75;
        if (Math.random() < chance && numMembers < maxMembers) {
          db.approve(i, user);
          numMembers++;
        }
      }
    }
  }

  private String generateRandomString() {
    StringBuilder s = new StringBuilder();
    Random r = new Random();
    int length = r.nextInt(5) + 5;
    for (int i = 0; i < length; i++) {
      final int numLetters = 26;
      s.append(String.valueOf((char) (r.nextInt(numLetters) + 'a')));
    }
    return s.toString();
  }

  private String generateRandomPhoneNumber() {
    StringBuilder s = new StringBuilder();
    final int numDigits = 10;
    for (int i = 0; i < numDigits; i++) {
      s.append((int) (Math.random() * numDigits));
    }
    return s.toString();
  }

  private String generateRandomTransportation() {
    final int methodOfTransportation = (int) (Math.random() * 3);
    switch (methodOfTransportation) {
      case 0:
        return "car";
      case 1:
        return "uber";
      default:
        return "lyft";
    }
  }

  private double generateRandomLat() {
    final int range = 180;
    final int minimum = -90;
    return Math.random() * range + minimum;
  }

  private double generateRandomLon() {
    final int range = 360;
    final int minimum = -180;
    return Math.random() * range + minimum;
  }

  private long generateRandomTime() {
    final int millisecondsPerSecond = 1000;
    final int secondsPerTenDays = 864000;
    long min = System.currentTimeMillis() / millisecondsPerSecond;
    long max = min + secondsPerTenDays;
    return (long) Math.floor(Math.random() * (max - min)) + min;
  }
}
