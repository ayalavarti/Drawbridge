package edu.brown.cs.drawbridge.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

public class DatabaseQuery {

  private Connection conn;
  private static final User DUMMY_USER = new User("0", "Mary",
      "mary@gmail.com");
  private static final Trip DUMMY_TRIP = Trip.TripBuilder.newTripBuilder()
      .addIdentification(0, "Mary's Carpool").addLocations(1.0, 2.0, 3.0, 4.0)
      .addTimes(500, 600).addDetails(7, 8.00, "555-867-5309", "Uber", "")
      .build();
  private static final String CREATE_USERS_TABLE = "CREATE IF NOT EXISTS users ("
      + "id TEXT NOT NULL PRIMARY KEY, " + "name TEXT, " + "email TEXT);";
  private static final String CREATE_TRIPS_TABLE = "CREATE IF NOT EXISTS trips ("
      + "id INTEGER NOT NULL IDENTITY PRIMARY KEY, " + "name TEXT, "
      + "start_latitude DOUBLE PRECISION, "
      + "start_longitude DOUBLE PRECISION, " + "end_latitude DOUBLE PRECISION, "
      + "end_longitude DOUBLE PRECISION, " + "departure BIGINT, "
      + "eta BIGINT, " + "max_people SMALLINT, " + "total_cost REAL, "
      + "host_phone TEXT, " + "transportation TEXT, " + "description TEXT);";
  private static final String CREATE_HOSTS_TABLE = "CREATE IF NOT EXISTS hosts ("
      + "user_id TEXT REFERENCES users(id), "
      + "trip_id INTEGER REFERENCES trips(id));";
  private static final String CREATE_MEMBERS_TABLE = "CREATE IF NOT EXISTS members ("
      + "user_id TEXT REFERENCES users(id), "
      + "trip_id INTEGER REFERENCES trips(id));";
  private static final String CREATE_REQUESTS_TABLE = "CREATE IF NOT EXISTS requests ("
      + "user_id TEXT REFERENCES users(id), "
      + "trip_id INTEGER REFERENCES trips(id));";
  private static final List<String> SETUP_QUERIES = new ArrayList<>(
      Arrays.asList(CREATE_USERS_TABLE, CREATE_TRIPS_TABLE, CREATE_HOSTS_TABLE,
          CREATE_MEMBERS_TABLE, CREATE_REQUESTS_TABLE));

  private static final String INSERT_USER = "INSERT INTO users VALUES (? ? ?);";
  private static final String INSERT_TRIP = "INSERT INTO trips(name, "
      + "start_latitude, start_longitude, end_latitude, end_longitude, "
      + "departure, eta, max_people, total_cost, host_phone, transportation, "
      + "description) VALUES (? ? ? ? ? ? ? ? ? ? ? ?) RETURNING id;";

  private static final String REMOVE_TRIP_BY_ID = "DELETE FROM trips WHERE id = ?;";
  private static final String REMOVE_TRIPS_BY_TIME = "DELETE FROM trips WHERE departure < current_timestamp;";
  private static final String INSERT_HOST = "INSERT INTO hosts VALUES (? ?);";
  private static final String INSERT_MEMBER = "INSERT INTO members VALUES (? ?);";
  private static final String INSERT_REQUEST = "INSERT INTO requests VALUES (? ?);";
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

  private static final String FIND_SIMILAR_TRIPS = "";// "SELECT * FROM trips
                                                      // WHERE ";
  private static final String FIND_CONNECTED_TRIPS = "";// "SELECT * FROM trips
                                                        // WHERE ";

  /**
   * A constructor based on the String name of the database.
   *
   * @param db
   *          The name of the database.
   * @throws ClassNotFoundException
   *           Errors involving the forName line.
   * @throws SQLException
   *           Errors involving the sql update.
   * @throws IOException
   *           Errors involving locating the database.
   */
  public DatabaseQuery(String db, String username, String password)
          throws ClassNotFoundException, SQLException {
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    Class.forName("org.postgresql.Driver");
    String urlToDB = "jdbc:postgresql:" + db;
    conn = DriverManager.getConnection(urlToDB, username, password);
    // these two lines tell the database to enforce foreign
    // keys during operations, and should be present
    try (Statement stat = conn.createStatement()) {
      stat.executeUpdate("PRAGMA foreign_keys = ON;");
    }
  }

  /**
   * Creates the necessary tables for the database.
   */
  public void setUp() {
    // make tables
    for (String query : SETUP_QUERIES) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.executeUpdate();
      } catch (SQLException e) {
        assert false;
      }
    }
  }

  /**
   * Finds the id of the user hosting the specified trip.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The String id of the host.
   */
  public String getHostOnTrip(int tripId) {
    String query = FIND_HOST_USER;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // try (ResultSet rs = prep.executeQuery()) {
    // if (rs.next()) {
    // return rs.getString(1);
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    return "0";
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
    String query = FIND_MEMBER_USERS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // results.add(rs.getString(1);
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add("0");
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
    String query = FIND_REQUEST_USERS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // results.add(rs.getString(1);
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add("0");
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
    String query = FIND_HOST_TRIPS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setString(1, userId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // results.add(rs.getInt(1);
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add(new Integer(0));
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
    String query = FIND_MEMBER_TRIPS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setString(1, userId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // results.add(rs.getInt(1);
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add(new Integer(0));
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
    String query = FIND_REQUEST_TRIPS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setString(1, userId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // results.add(rs.getInt(1);
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add(new Integer(0));
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
    String query = FIND_USER_BY_ID;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setString(1, userId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // return new User(userId, rs.getString(1), rs.getString(2),
    // getHostTripsWithUser(userId), getMemberTripsWithUser(userId),
    // getRequestTripsWithUser(userId));
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    return DUMMY_USER;
  }

  /**
   * Finds the information for a specific Trip by id.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The Trip object with the specified id.
   */
  public Trip getTripById(int tripId) {
    String query = FIND_TRIP_BY_ID;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // try (ResultSet rs = prep.executeQuery()) {
    // while (rs.next()) {
    // return new Trip(tripId, rs.getString(1), rs.getDouble(2),
    // rs.getDouble(3),
    // rs.getDouble(4), rs.getDouble(5), rs.getInt(6), rs.getInt(7),
    // rs.getInt(8), rs.getDouble(9), rs.getString(10), rs.getString(11),
    // rs.getString(12), getHostOnTrip(tripId), getMembersOnTrip(tripId),
    // getRequestsOnTrip(tripId));
    // }
    // }
    // } catch (SQLException e) {
    // assert false;
    // }
    return DUMMY_TRIP;
  }

  /**
   * Adds a new User to the database.
   *
   * @param user
   *          The User object to add.
   * @return True if the user was successfully added. False otherwise.
   */
  public boolean addUser(User user) {
    String query = INSERT_USER;
    try (PreparedStatement prep = conn.prepareStatement(query)) {
      prep.setString(1, user.getId());
      prep.setString(2, user.getName());
      prep.setString(3, user.getEmail());
      prep.addBatch();
      prep.executeUpdate();
      return true;
    } catch (SQLException e) {
      return false;
    }
    // return true;
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
    String query = INSERT_TRIP;
    // int tripId = -1;
    // try (PreparedStatement prep = conn.prepareStatement(query,
    // Statement.RETURN_GENERATED_KEYS)) {
    // prep.setString(1, trip.getName());
    // prep.setDouble(2, trip.getStartingLongitude());
    // prep.setDouble(3, trip.getStartingLatitude());
    // prep.setDouble(4, trip.getEndingLongitude());
    // prep.setDouble(5, trip.getEndingLatitude());
    // prep.setInt(6, trip.getDepartureTime());
    // prep.setInt(7, trip.getEta());
    // prep.setInt(8, trip.getMaxUsers());
    // prep.setDouble(9, trip.getCost());
    // prep.setString(10, trip.getPhoneNumber());
    // prep.setString(11, trip.getMethodOfTransportation());
    // prep.setString(12, trip.getComments());
    // prep.addBatch();
    // try (ResultSet rs = prep.executeQuery()) {
    // if (rs.next()) {
    // tripId = rs.getInt(1);
    // }
    // }
    // } catch (SQLException e) {
    // return false;
    // }
    query = INSERT_HOST; // insert into host
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setString(1, hostId);
    // prep.setInt(2, tripId);
    // prep.addBatch();
    // prep.executeUpdate();
    // } catch (SQLException e) {
    // deleteTripManually(tripId);
    // return false;
    // }
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
    String query = REMOVE_TRIP_BY_ID;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // prep.executeUpdate();
    // return true;
    // } catch (SQLException e) {
    // return false;
    // }
    return true;
  }

  /**
   * Deletes all trips that have already departed from the database.
   *
   * @return True if all expired trips were deleted successfully. False
   *         otherwise.
   */
  public boolean deleteExpiredTrips() {
    String query = REMOVE_TRIPS_BY_TIME;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.executeUpdate();
    // return true;
    // } catch (SQLException e) {
    // return false;
    // }
    return true;
  }

  /**
   * Finds all of the trips that match the search criteria.
   *
   * @param trip
   *          The Trip information being matched.
   * @param walkRadius
   *          The buffer for matching the requested start and end locations.
   * @param timeBuffer
   *          The buffer for matching the requested departure time.
   * @return A List of all the relevant trips in the database.
   */
  public List<Trip> searchRelevantTrips(Trip trip, double walkRadius,
      int timeBuffer) {
    List<Trip> results = new ArrayList<>();
    String query = FIND_SIMILAR_TRIPS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    //
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add(DUMMY_TRIP);
    return results;
  }

  /**
   * Finds all of the trips that are graphically connected to the input trip.
   * That is, trips that depart near the given destination and that depart after
   * the given departure time within a specific time frame.
   *
   * @param trip
   *          The previous Trip.
   * @param walkRadius
   *          The buffer for finding reasonably distanced trips.
   * @param timeBuffer
   *          The buffer for finding reasonably timed trips.
   * @return A List of all the trips connected to the given trip.
   */
  public List<Trip> getConnectedTrips(Trip trip, double walkRadius,
      int timeBuffer) {
    List<Trip> results = new ArrayList<>();
    String query = FIND_CONNECTED_TRIPS;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    //
    // } catch (SQLException e) {
    // assert false;
    // }
    results.add(DUMMY_TRIP);
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
    String query = INSERT_REQUEST;
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // prep.setString(2,userId);
    // prep.addBatch();
    // prep.executeUpdate();
    // return true;
    // } catch (SQLException e) {
    // return false;
    // }
    return true;
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
    String query = INSERT_MEMBER; // insert into members
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // prep.setString(2,userId);
    // prep.addBatch();
    // prep.executeUpdate();
    // } catch (SQLException e) {
    // return false;
    // }
    query = REMOVE_REQUEST; // delete from requests
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // prep.setString(2,userId);
    // prep.executeUpdate();
    // return true;
    // } catch (SQLException e) {
    // return false;
    // }
    return true;
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
    // String query = REMOVE_REQUEST; //delete from requests
    // try (PreparedStatement prep = conn.prepareStatement(query)) {
    // prep.setInt(1, tripId);
    // prep.setString(2,userId);
    // prep.executeUpdate();
    // return true;
    // } catch (SQLException e) {
    // return false;
    // }
    return true;
  }
}
