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
    String username = "dev";
    String password = "dev";
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

  private long generateRandomTimeInRange(long min, long max) {
    return (long) (Math.floor(Math.random() * (max - min)) + min);
  }

  private String[] startNames = {"Keeney Quad", "Providence Station",
          "Metcalf Research Building", "RISD Store",
          "CIT", "Hope High School", "Wheeler School", "Nelson Fitness Center",
          "Providence Place", "Ratty", "Jo's"};
  private double[][] startCoordinates =
          {
                  {-71.4032918, 41.8247879},
                  {-71.41449, 41.828762},
                  {-71.4007193, 41.8267941},
                  {-71.40683, 41.8256618},
                  {-71.3995396, 41.8271697},
                  {-71.40237, 41.834724},
                  {-71.39872, 41.828613},
                  {-71.3979893, 41.8296967},
                  {-71.41631, 41.827362},
                  {-71.40083, 41.825184},
                  {-71.39944, 41.82346}
          };
  private String[] endNames = {"Copley Square", "Metropolitan Opera House",
          "Museum of Fine Arts, Boston", "Rockefeller Center", "Coney Island",
          "Six Flags, New England", "Mount Monadnock", "Narragansett Beach",
          "Tanglewood", "Grand Central Station", "Reading Terminal",
          "Walden Pond"};
  //to add: Kimball's Farm, Bedford Farms, Reasons to be Cheerful
  private double[][] endCoordinates =
          {
                  {-71.078705, 42.34819},
                  {-73.98343, 40.772377},
                  {-71.094734, 42.33818},
                  {-73.97852, 40.75865},
                  {-73.9851, 40.5758},
                  {-72.61579, 42.037796},
                  {-72.0615, 42.83001},
                  {-71.4563, 41.4378},
                  {-73.310844, 42.349247},
                  {-73.97745, 40.752785},
                  {-75.15931, 39.953342},
                  {-71.33524, 42.440456}
          };
  private User[] users = {
          new User("108134993267513125002", "Arvind", "arvind_yalavarti@brown.edu"),
          new User("105528985214845949817", "Jeff", "jeffreyzhu101@gmail.com"),
          new User("118428670975676923422", "Mark", "lavrema@outlook.com"),
          new User("106748572580441940868", "Sam", "samuel_maffa@brown.edu"),
          new User("500", "Blueno", "blue@no.com"),
  };
  private String[] comments = {"", "Looking for some more members", "Anybody want to join me?"};

  private void populateTrips2(DatabaseQuery db, int tripNum)
          throws SQLException, MissingDataException, ClassNotFoundException {
    for (int i = 1; i <= tripNum; i++) {
      int randomStartIndex = (int) (Math.random() * startNames.length);
      int randomEndIndex = (int) (Math.random() * endNames.length);
      int randomHost = (int) (Math.random() * users.length);
      long randomTime = generateRandomTimeInRange(1557154980, 1558018980);
      long randomTripLength = generateRandomTimeInRange(7200, 14400);
      int randomTripSize = 2 + (int) (Math.random() * 4);
      double randomCost = 20 + Math.random() * 20;
      String randomComment = comments[(int) (Math.random() * comments.length)];
      Trip t = Trip.TripBuilder.newTripBuilder()
              .addIdentification(-1, startNames[randomStartIndex] + " to " + endNames[randomEndIndex])
              .addLocations(startCoordinates[randomStartIndex][1], startCoordinates[randomStartIndex][0],
                      endCoordinates[randomEndIndex][1], endCoordinates[randomEndIndex][0])
              .addAddressNames(startNames[randomStartIndex], endNames[randomEndIndex])
              .addTimes(randomTime, randomTime + randomTripLength)
              .addDetails(randomTripSize, randomCost, generateRandomPhoneNumber(), generateRandomTransportation(), randomComment)
              .build();
      db.createTrip(t, users[randomHost].getId());
    }
  }

  //@Test
  public void popUsers() throws SQLException, ClassNotFoundException {
    test = new DatabaseQuery("//127.0.0.1:5432/carpools", "dev", "dev");
    for (User u : users) {
      test.addUser(u);
    }
  }

  //@Test
  public void popTrips() throws SQLException, ClassNotFoundException, MissingDataException {
    test = new DatabaseQuery("//127.0.0.1:5432/carpools", "dev", "dev");
    populateTrips2(test, 1000);
  }

  //@Test
  public void popMembers() throws SQLException, ClassNotFoundException, MissingDataException {
    test = new DatabaseQuery("//127.0.0.1:5432/carpools", "dev", "dev");
    for (int i = 2001; i < 3000; i++) {
      Trip t = test.getTripById(i);
      String id = t.getHostId();
      int maxPeople = t.getMaxUsers();
      int numPeople = 1;
      for (User u : users) {
        boolean accept = new Random().nextBoolean();
        if (!id.equals(u.getId())) {
          test.request(i, u.getId());
          if (accept && numPeople < maxPeople) {
            test.approve(i, u.getId());
            numPeople++;
          }
        }
      }
    }
  }
}
