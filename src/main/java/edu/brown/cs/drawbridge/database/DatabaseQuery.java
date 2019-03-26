package edu.brown.cs.drawbridge.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

public class DatabaseQuery {

  private Connection conn;
  public static final User DUMMY_USER = new User("0", "Mary",
      "mary@gmail.com");
  public static final Trip DUMMY_TRIP = Trip.TripBuilder.newTripBuilder()
      .addIdentification(0, "Mary's Carpool").addLocations(1.0, 2.0, 3.0, 4.0)
      .addAddressNames("start", "end").addTimes(500, 600)
      .addDetails(7, 8.00, "555-867-5309", "Uber", "").build();

  private static final String INSERT_USER = "INSERT INTO users VALUES (?, ?, ?);";
  private static final String INSERT_TRIP = "INSERT INTO trips(name, start_name,"
      + "start_latitude, start_longitude, end_name, end_latitude, end_longitude, "
      + "departure, eta, max_people, total_cost, phone_number, transportation, "
      + "description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

  private static final String REMOVE_TRIP_BY_ID = "DELETE FROM trips WHERE id = ?;";
  private static final String REMOVE_TRIPS_BY_TIME = "DELETE FROM trips WHERE "
          + "departure < EXTRACT(EPOCH FROM current_timestamp);";
  private static final String INSERT_HOST = "INSERT INTO hosts VALUES (?, ?);";
  private static final String INSERT_MEMBER = "INSERT INTO members VALUES (?, ?);";
  private static final String INSERT_REQUEST = "INSERT INTO requests VALUES (?, ?);";
  private static final String REMOVE_MEMBER = "DELETE FROM members WHERE trip_id = ? AND user_id = ?;";
  private static final String REMOVE_REQUEST = "DELETE FROM requests WHERE trip_id = ? AND user_id = ?;";

  private static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
  private static final String FIND_TRIP_BY_ID = "SELECT * FROM trips WHERE id = ?;";
  private static final String FIND_HOST_TRIPS = "SELECT trip_id FROM hosts WHERE user_id = ?";
  private static final String FIND_MEMBER_TRIPS = "SELECT trip_id FROM members WHERE user_id = ?";
  private static final String FIND_REQUEST_TRIPS = "SELECT trip_id FROM requests WHERE user_id = ?";
  private static final String FIND_HOST_USER = "SELECT user_id FROM hosts WHERE trip_id = ?";
  private static final String FIND_MEMBER_USERS = "SELECT user_id FROM members WHERE trip_id = ?";
  private static final String FIND_REQUEST_USERS = "SELECT user_id FROM requests WHERE trip_id = ?";

  private static final String FIND_SIMILAR_TRIPS = "SELECT * FROM trips WHERE "
          + "((start_latitude - ?)^2 + (start_longitude - ?)^2 <= (?)^2) AND "
          + "((end_latitude - ?)^2 + (end_longitude - ?)^2 <= (?)^2) "
          + "AND (departure BETWEEN ? AND ?);";

  private static final String FIND_CONNECTED_TRIPS = "SELECT * FROM trips WHERE "
          + "((start_latitude - ?)^2 + (start_longitude - ?)^2 <= (?)^2) "
          + "AND (departure BETWEEN ? AND ?);";



  /**
   * A constructor based on the String name of the database.
   *
   * @param db
   *          The name of the database.
   * @param username The username used to access the database.
   * @param password The password used to access the database.
   * @throws ClassNotFoundException
   *           Errors involving the forName line.
   * @throws SQLException
   *           Errors involving the sql update.
   */
  public DatabaseQuery(String db, String username, String password)
          throws ClassNotFoundException, SQLException {
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.postgresql.Driver");
    String urlToDB = "jdbc:postgresql:" + db;
    conn = DriverManager.getConnection(urlToDB, username, password);
  }

  /**
   * Finds the id of the user hosting the specified trip.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The String id of the host.
   */
  public String getHostOnTrip(int tripId) {
    try (PreparedStatement prep = conn.prepareStatement(FIND_HOST_USER)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          return rs.getString(1);
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return null;
  }

  /**
   * Finds the ids of all the users that are confirmed members of the specified
   * trip.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The List of String ids of the trip's members.
   */
  public List<String> getMembersOnTrip(int tripId) {
    List<String> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_MEMBER_USERS)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getString(1));
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Finds the ids of all the users requesting to join the specified trip.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The List of String ids of all requesting users for that trip.
   */
  public List<String> getRequestsOnTrip(int tripId) {
    List<String> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_REQUEST_USERS)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getString(1));
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Finds the ids of all the trips that the specified user is hosting.
   *
   * @param userId
   *          The String id of the user.
   * @return The List of Integer ids of all trips hosted by the user.
   */
  public List<Integer> getHostTripsWithUser(String userId) {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_HOST_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Finds the ids of all the trips that the specified user is a confirmed
   * member of.
   *
   * @param userId
   *          The String id of the user.
   * @return The List of Integer ids of all trips that the user is a member of.
   */
  public List<Integer> getMemberTripsWithUser(String userId) {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_MEMBER_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Finds the ids of all the trips that the specified user is requesting to
   * join.
   *
   * @param userId
   *          The String id of the user.
   * @return The List of Integer ids of all trips that the user is requesting to
   *         join.
   */
  public List<Integer> getRequestTripsWithUser(String userId) {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_REQUEST_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Finds the information for a specific User by id.
   *
   * @param userId
   *          The String id of the user.
   * @return The User object with the specified id.
   */
  public User getUserById(String userId) {
    try (PreparedStatement prep = conn.prepareStatement(FIND_USER_BY_ID)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          User u = new User(userId, rs.getString(1), rs.getString(2));
          u.setTrips(getHostTripsWithUser(userId), getMemberTripsWithUser(userId),
                  getRequestTripsWithUser(userId));
          return u;
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return null;
  }

  /**
   * Finds the information for a specific Trip by id.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The Trip object with the specified id.
   */
  public Trip getTripById(int tripId) {
    try (PreparedStatement prep = conn.prepareStatement(FIND_TRIP_BY_ID)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          return Trip.TripBuilder.newTripBuilder()
                  .addIdentification(tripId, rs.getString(2))
                  .addLocations(rs.getDouble(4), rs.getDouble(5),
                          rs.getDouble(7), rs.getDouble(8))
                  .addAddressNames(rs.getString(3), rs.getString(6))
                  .addTimes(rs.getInt(9), rs.getInt(10))
                  .addDetails(rs.getInt(11), rs.getDouble(12),
                          rs.getString(13), rs.getString(14), rs.getString(15))
                  .buildWithUsers(getHostOnTrip(tripId), getMembersOnTrip(tripId), getRequestsOnTrip(tripId));
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return null;
  }

  /**
   * Adds a new User to the database.
   *
   * @param user
   *          The User object to add.
   * @return True if the user was successfully added. False otherwise.
   */
  public boolean addUser(User user) {
    if (getUserById(user.getId()) != null) {
      return true;
    }
    try (PreparedStatement prep = conn.prepareStatement(INSERT_USER)) {
      prep.setString(1, user.getId());
      prep.setString(2, user.getName());
      prep.setString(3, user.getEmail());
      prep.addBatch();
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Adds a new Trip to the databse.
   *
   * @param trip
   *          The Trip object to add.
   * @param hostId
   *          The String id of the user hosting the trip.
   * @return True if the trip and host relation were successfully added. False
   *         otherwise.
   */
  public boolean createTrip(Trip trip, String hostId) {
    if (getUserById(hostId) == null) {
      return false;
    }
    //insert into trip
    int tripId = -1;
    try (PreparedStatement prep = conn.prepareStatement(INSERT_TRIP,
            Statement.RETURN_GENERATED_KEYS)) {
      prep.setString(1, trip.getName());
      prep.setString(2, trip.getStartingAddress());
      prep.setDouble(3, trip.getStartingLatitude());
      prep.setDouble(4, trip.getStartingLongitude());
      prep.setString(5, trip.getEndingAddress());
      prep.setDouble(6, trip.getEndingLatitude());
      prep.setDouble(7, trip.getEndingLongitude());
      prep.setInt(8, trip.getDepartureTime());
      prep.setInt(9, trip.getEta());
      prep.setInt(10, trip.getMaxUsers());
      prep.setDouble(11, trip.getCost());
      prep.setString(12, trip.getPhoneNumber());
      prep.setString(13, trip.getMethodOfTransportation());
      prep.setString(14, trip.getComments());
      prep.addBatch();
      prep.executeUpdate();
      try (ResultSet rs = prep.getGeneratedKeys()) {
        if (rs.next()) {
          tripId = rs.getInt(1);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    //insert into host
    try (PreparedStatement prep = conn.prepareStatement(INSERT_HOST)) {
      prep.setString(1, hostId);
      prep.setInt(2, tripId);
      prep.addBatch();
      prep.executeUpdate();
    } catch (SQLException e) {
      deleteTripManually(tripId);
      return false;
    }
    return true;
  }

  /**
   * Deletes the trip with the specified id from the database.
   *
   * @param tripId
   *          The int id of the trip.
   * @return True if the trip was deleted successfully. False otherwise.
   */
  public boolean deleteTripManually(int tripId) {
    try (PreparedStatement prep = conn.prepareStatement(REMOVE_TRIP_BY_ID)) {
      prep.setInt(1, tripId);
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Deletes all trips that have already departed from the database.
   *
   * @return True if all expired trips were deleted successfully. False
   *         otherwise.
   */
  public boolean deleteExpiredTrips() {
    try (PreparedStatement prep = conn.prepareStatement(REMOVE_TRIPS_BY_TIME)) {
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Finds all of the trips that match the search criteria.
   * @param startLat The double starting latitude.
   * @param startLon The double starting longitude.
   * @param endLat The double ending latitude.
   * @param endLong The double ending longitude.
   * @param departure The int time of departure in epoch time
   * @param walkRadius The buffer for matching the requested start and end locations.
   * @param timeBuffer The buffer for matching the requested departure time.
   * @return A List of all the relevant trips in the database.
   */
  public List<Trip> searchRelevantTrips(
          double startLat, double startLon, double endLat, double endLong,
          int departure, double walkRadius, int timeBuffer) {
    List<Trip> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_SIMILAR_TRIPS)) {
      prep.setDouble(1, startLat);
      prep.setDouble(2, startLon);
      prep.setDouble(3, walkRadius);
      prep.setDouble(4, endLat);
      prep.setDouble(5, endLong);
      prep.setDouble(6, walkRadius);
      prep.setInt(7, departure - timeBuffer);
      prep.setInt(8, departure + timeBuffer);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(Trip.TripBuilder.newTripBuilder()
                  .addIdentification(rs.getInt(1), rs.getString(2))
                  .addLocations(rs.getDouble(4), rs.getDouble(5),
                          rs.getDouble(7), rs.getDouble(8))
                  .addAddressNames(rs.getString(3), rs.getString(6))
                  .addTimes(rs.getInt(9), rs.getInt(10))
                  .addDetails(rs.getInt(11), rs.getDouble(12),
                          rs.getString(13), rs.getString(14), rs.getString(15))
                  .build());
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Finds all of the trips that are graphically connected to the input trip.
   * That is, trips that depart near the given destination and that depart after
   * the given departure time within a specific time frame.
   *
   * @param lastEndLat The double ending latitude of the previous trip.
   * @param lastEndLon The double ending longitude of the previous trip.
   * @param walkRadius
   *          The buffer for finding reasonably distanced trips.
   * @param lastEta The int expected arrival time of the last trip.
   * @param timeBuffer
   *          The buffer for finding reasonably timed trips.
   * @return A List of all the trips connected to the given trip.
   */
  public List<Trip> getConnectedTrips(
          double lastEndLat, double lastEndLon, double walkRadius,
          int lastEta, int timeBuffer) {
    List<Trip> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(FIND_CONNECTED_TRIPS)) {
      prep.setDouble(1, lastEndLat);
      prep.setDouble(2, lastEndLon);
      prep.setDouble(3, walkRadius);
      prep.setInt(4, lastEta);
      prep.setInt(5, lastEta + timeBuffer);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(Trip.TripBuilder.newTripBuilder()
                  .addIdentification(rs.getInt(1), rs.getString(2))
                  .addLocations(rs.getDouble(4), rs.getDouble(5),
                          rs.getDouble(7), rs.getDouble(8))
                  .addAddressNames(rs.getString(3), rs.getString(6))
                  .addTimes(rs.getInt(9), rs.getInt(10))
                  .addDetails(rs.getInt(11), rs.getDouble(12),
                          rs.getString(13), rs.getString(14), rs.getString(15))
                  .build());
        }
      }
    } catch (SQLException e) {
      assert false;
    }
    return results;
  }

  /**
   * Inserts a request relation into the database.
   *
   * @param tripId
   *          The int id of the trip being requested.
   * @param userId
   *          The String id of the user requesting to join the trip.
   * @return True if the request was processed successfully. False otherwise.
   */
  public boolean request(int tripId, String userId) {
    try (PreparedStatement prep = conn.prepareStatement(INSERT_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.addBatch();
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Approves a request by inserting a member relation while removing its
   * corresponding request relation.
   *
   * @param tripId
   *          The int id of the trip being requested.
   * @param userId
   *          The String id of the user requesting to join the trip.
   * @return True if the approval was processed successfully. False otherwise.
   */
  public boolean approve(int tripId, String userId) {
    //insert into members
    try (PreparedStatement prep = conn.prepareStatement(INSERT_MEMBER)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.addBatch();
      prep.executeUpdate();
    } catch (SQLException e) {
      return false;
    }
    //delete from requests
    try (PreparedStatement prep = conn.prepareStatement(REMOVE_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Rejects a request by removing its relation from the database.
   *
   * @param tripId
   *          The int id of the trip being requested.
   * @param userId
   *          The String id of the user requesting to join the trip.
   * @return True if the rejection was processed successfully. False otherwise.
   */
  public boolean reject(int tripId, String userId) {
    try (PreparedStatement prep = conn.prepareStatement(REMOVE_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Kicks a member by removing its relation from the database.
   *
   * @param tripId
   *          The int id of the trip.
   * @param userId
   *          The String id of the user being kicked from the trip.
   * @return True if the kick was processed successfully. False otherwise.
   */
  public boolean kick(int tripId, String userId) {
    try (PreparedStatement prep = conn.prepareStatement(REMOVE_MEMBER)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }
}
