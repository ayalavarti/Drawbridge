package edu.brown.cs.drawbridge.database;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for PostgreSQL database queries.
 *
 * @author Sam Maffa
 */
public class DatabaseQuery {

  private static final double NANOMETER = 0.000000000001;
  private Connection conn;

  /**
   * A constructor based on the String name of the database.
   *
   * @param db
   *     The name of the database.
   * @param username
   *     The username used to access the database.
   * @param password
   *     The password used to access the database.
   *
   * @throws ClassNotFoundException
   *     Errors involving the forName line.
   * @throws SQLException
   *     Errors involving the sql update.
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
   *     The int id of the trip.
   *
   * @return The String id of the host.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public String getHostOnTrip(int tripId)
      throws SQLException, MissingDataException {

    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_HOST_USER)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          return rs.getString(1);
        }
      }
    }
    throw new MissingDataException("Trip has no host");
  }

  /**
   * Finds the ids of all the users that are confirmed members of the specified
   * trip.
   *
   * @param tripId
   *     The int id of the trip.
   *
   * @return The List of String ids of the trip's members.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public List<String> getMembersOnTrip(int tripId) throws SQLException {
    List<String> results = new ArrayList<>();
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_MEMBER_USERS)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getString(1));
        }
      }
    }
    return results;
  }

  /**
   * Finds the ids of all the users requesting to join the specified trip.
   *
   * @param tripId
   *     The int id of the trip.
   *
   * @return The List of String ids of all requesting users for that trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public List<String> getRequestsOnTrip(int tripId) throws SQLException {
    List<String> results = new ArrayList<>();
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_REQUEST_USERS)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getString(1));
        }
      }
    }
    return results;
  }

  /**
   * Finds the ids of all the trips that the specified user is hosting.
   *
   * @param userId
   *     The String id of the user.
   *
   * @return The List of Integer ids of all trips hosted by the user.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public List<Integer> getHostTripsWithUser(String userId) throws SQLException {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_HOST_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    }
    return results;
  }

  /**
   * Finds the ids of all the trips that the specified user is a confirmed
   * member of.
   *
   * @param userId
   *     The String id of the user.
   *
   * @return The List of Integer ids of all trips that the user is a member of.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public List<Integer> getMemberTripsWithUser(String userId)
      throws SQLException {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_MEMBER_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    }
    return results;
  }

  /**
   * Finds the ids of all the trips that the specified user is requesting to
   * join.
   *
   * @param userId
   *     The String id of the user.
   *
   * @return The List of Integer ids of all trips that the user is requesting to
   *     join.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public List<Integer> getRequestTripsWithUser(String userId)
      throws SQLException {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_REQUEST_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    }
    return results;
  }

  /**
   * Finds the information for a specific User by id.
   *
   * @param userId
   *     The String id of the user.
   *
   * @return The User object with the specified id.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public User getUserById(String userId)
      throws SQLException, MissingDataException {

    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_USER_BY_ID)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          User u = new User(userId,
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3));
          u.setTrips(getHostTripsWithUser(userId),
              getMemberTripsWithUser(userId), getRequestTripsWithUser(userId));
          return u;
        }
      }
    }
    throw new MissingDataException("User does not exist");
  }

  /**
   * Finds the information for a specific Trip by id.
   *
   * @param tripId
   *     The int id of the trip.
   *
   * @return The Trip object with the specified id.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public Trip getTripById(int tripId)
      throws SQLException, MissingDataException {

    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_TRIP_BY_ID)) {
      prep.setInt(1, tripId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          return Trip.TripBuilder.newTripBuilder()
              .addIdentification(tripId, rs.getString(1))
              .addLocations(rs.getDouble(3), rs.getDouble(4), rs.getDouble(6),
                  rs.getDouble(7))
              .addAddressNames(rs.getString(2), rs.getString(5))
              .addTimes(rs.getLong(8), rs.getLong(9))
              .addDetails(rs.getInt(10), rs.getDouble(11), rs.getString(12),
                  rs.getString(13), rs.getString(14))
              .buildWithUsers(getHostOnTrip(tripId), getMembersOnTrip(tripId),
                  getRequestsOnTrip(tripId));
        }
      }
    }
    throw new MissingDataException("Trip does not exist");
  }

  /**
   * Adds a new User to the database.
   *
   * @param user
   *     The User object to add.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void addUser(User user) throws SQLException {
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.INSERT_USER)) {
      prep.setString(1, user.getId());
      prep.setString(2, user.getName());
      prep.setString(3, user.getEmail());
      prep.setString(4, user.getProfilePic());
      prep.addBatch();
      prep.executeUpdate();
    }
  }

  /**
   * Adds a new Trip to the databse.
   *
   * @param trip
   *     The Trip object to add.
   * @param hostId
   *     The String id of the user hosting the trip.
   *
   * @return The database's id of the inserted trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public int createTrip(Trip trip, String hostId)
      throws SQLException, MissingDataException {
    int tripId = -1;
    getUserById(hostId);
    // insert into trip
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.INSERT_TRIP,
            Statement.RETURN_GENERATED_KEYS)) {
      prep.setString(1, trip.getName());
      prep.setString(2, trip.getStartingAddress());
      prep.setDouble(3, trip.getStartingLatitude());
      prep.setDouble(4, trip.getStartingLongitude());
      prep.setString(5, trip.getEndingAddress());
      prep.setDouble(6, trip.getEndingLatitude());
      prep.setDouble(7, trip.getEndingLongitude());
      prep.setLong(8, trip.getDepartureTime());
      prep.setLong(9, trip.getEta());
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
    }
    // insert into host
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.INSERT_HOST)) {
      prep.setInt(1, tripId);
      prep.setString(2, hostId);
      prep.addBatch();
      prep.executeUpdate();
    } catch (SQLException e) {
      deleteTripManually(tripId);
      throw e;
    }
    return tripId;
  }

  /**
   * Deletes the trip with the specified id from the database.
   *
   * @param tripId
   *     The int id of the trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void deleteTripManually(int tripId) throws SQLException {
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.REMOVE_TRIP_BY_ID)) {
      prep.setInt(1, tripId);
      prep.executeUpdate();
    }
  }

  /**
   * Deletes all trips that have already departed from the database.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void deleteExpiredTrips() throws SQLException {
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.REMOVE_TRIPS_BY_TIME)) {
      prep.executeUpdate();
    }
  }

  /**
   * Finds all of the trips that are graphically connected to the input trip.
   * That is, trips that depart near the given destination and that depart after
   * the given departure time within a specific time frame.
   *
   * @param lastLat
   *     The double ending latitude of the previous trip.
   * @param lastLon
   *     The double ending longitude of the previous trip.
   * @param walkRadius
   *     The buffer for finding reasonably distanced trips.
   * @param start
   *          The long beginning of the time window in epoch time.
   * @param end
   *          The long end of the time window in epoch time.
   *
   * @return A List of all the trips connected to the given trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public List<Trip> searchTripsByTimeWindow(double lastLat, double lastLon,
      double walkRadius, double start, double end)
      throws SQLException, MissingDataException {
    List<Trip> results = new ArrayList<>();
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.FIND_CONNECTED_TRIPS_BY_WINDOW)) {
      prep.setDouble(1, lastLat);
      prep.setDouble(2, lastLat);
      prep.setDouble(3, lastLon);
      prep.setDouble(4, walkRadius);
      prep.setDouble(5, start);
      prep.setDouble(6, end);

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(Trip.TripBuilder.newTripBuilder()
              .addIdentification(rs.getInt(1), rs.getString(2))
              .addLocations(rs.getDouble(4), rs.getDouble(5), rs.getDouble(7),
                  rs.getDouble(8))
              .addAddressNames(rs.getString(3), rs.getString(6))
              .addTimes(rs.getLong(9), rs.getLong(10))
              .addDetails(rs.getInt(11), rs.getDouble(12), rs.getString(13),
                  rs.getString(14), rs.getString(15))
              .buildWithUsers(getHostOnTrip(rs.getInt(1)),
                  getMembersOnTrip(rs.getInt(1)),
                  getRequestsOnTrip(rs.getInt(1))));
        }
      }
    } catch (MissingDataException e) {
      throw new MissingDataException("Results include trips without hosts");
    }
    return results;
  }

  /**
   * Finds all of the trips that are graphically connected to the input trip.
   * That is, trips that depart near the given destination and that depart after
   * the given departure time within a specific time frame.
   *
   * @param lastLat
   *     The double ending latitude of the previous trip.
   * @param lastLon
   *     The double ending longitude of the previous trip.
   * @param walkRadius
   *     The buffer for finding reasonably distanced trips.
   * @param lastEta
   *          The long expected arrival time of the last trip.
   * @param timeBuffer
   *     The buffer for finding reasonably timed trips.
   *
   * @return A List of all the trips connected to the given trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public List<Trip> getConnectedTripsAfterEta(double lastLat, double lastLon,
                                              double walkRadius, long lastEta, double timeBuffer)
          throws SQLException, MissingDataException {
    //due to rounding errors in SQL, a walk radius of 0 will not return results.
    if (walkRadius == 0) {
      walkRadius = NANOMETER;
    }
    List<Trip> results = new ArrayList<>();
    try (PreparedStatement prep = conn
            .prepareStatement(QueryStrings.FIND_CONNECTED_TRIPS_AFTER_ETA)) {
      prep.setDouble(1, lastLat);
      prep.setDouble(2, lastLon);
      prep.setDouble(3, walkRadius);
      prep.setDouble(4, lastLat);
      prep.setDouble(5, lastLon);
      prep.setLong(6, lastEta);
      prep.setDouble(7, lastLat);
      prep.setDouble(8, lastLon);
      prep.setLong(9, (long) (lastEta + timeBuffer));

      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(Trip.TripBuilder.newTripBuilder()
                  .addIdentification(rs.getInt(1), rs.getString(2))
                  .addLocations(rs.getDouble(4), rs.getDouble(5), rs.getDouble(7),
                          rs.getDouble(8))
                  .addAddressNames(rs.getString(3), rs.getString(6))
                  .addTimes(rs.getLong(9), rs.getLong(10))
                  .addDetails(rs.getInt(11), rs.getDouble(12), rs.getString(13),
                          rs.getString(14), rs.getString(15))
                  .buildWithUsers(getHostOnTrip(rs.getInt(1)),
                          getMembersOnTrip(rs.getInt(1)),
                          getRequestsOnTrip(rs.getInt(1))));
        }
      }
    } catch (MissingDataException e) {
      throw new MissingDataException("Results include trips without hosts");
    }
    return results;
  }

  /**
   * Finds all of the trips that match the search criteria based on departure.
   *
   * @param lat
   *     The double latitude of departure.
   * @param lon
   *     The double longitude of departure.
   * @param walkRadius
   *     The buffer for finding reasonably distanced trips.
   * @param departure
   *          The time of departure.
   * @param timeBuffer
   *     The buffer for finding reasonably timed trips.
   *
   * @return A List of all the trips leaving around the specified location and
   *     time.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   * @throws MissingDataException
   *     Errors involving the database's contents.
   */
  public List<Trip> getConnectedTripsWithinTimeRadius(double lat, double lon,
      double walkRadius, long departure, double timeBuffer)

      throws SQLException, MissingDataException {
    return searchTripsByTimeWindow(lat, lon, walkRadius, departure - timeBuffer,
        departure + timeBuffer);
  }

  /**
   * Inserts a request relation into the database.
   *
   * @param tripId
   *     The int id of the trip being requested.
   * @param userId
   *     The String id of the user requesting to join the trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void request(int tripId, String userId) throws SQLException {
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.INSERT_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2, userId);
      prep.addBatch();
      prep.executeUpdate();
    }
  }

  /**
   * Approves a request by inserting a member relation while removing its
   * corresponding request relation.
   *
   * @param tripId
   *     The int id of the trip being requested.
   * @param userId
   *     The String id of the user requesting to join the trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void approve(int tripId, String userId) throws SQLException {
    // insert into members
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.INSERT_MEMBER)) {
      prep.setInt(1, tripId);
      prep.setString(2, userId);
      prep.addBatch();
      prep.executeUpdate();
    }
    // delete from requests
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.REMOVE_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2, userId);
      prep.executeUpdate();
    } catch (SQLException e) {
      kick(tripId, userId);
      throw e;
    }
  }

  /**
   * Rejects a request by removing its relation from the database.
   *
   * @param tripId
   *     The int id of the trip being requested.
   * @param userId
   *     The String id of the user requesting to join the trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void reject(int tripId, String userId) throws SQLException {
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.REMOVE_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2, userId);
      prep.executeUpdate();
    }
  }

  /**
   * Kicks a member by removing its relation from the database.
   *
   * @param tripId
   *     The int id of the trip.
   * @param userId
   *     The String id of the user being kicked from the trip.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void kick(int tripId, String userId) throws SQLException {
    try (PreparedStatement prep = conn
        .prepareStatement(QueryStrings.REMOVE_MEMBER)) {
      prep.setInt(1, tripId);
      prep.setString(2, userId);
      prep.executeUpdate();
    }
  }

  /**
   * Clears all data from the database.
   *
   * @throws SQLException
   *     Errors involving SQL queries.
   */
  public void clearData() throws SQLException {
    List<String> deleteQueries = new ArrayList<>(Arrays
        .asList("DELETE FROM requests;", "DELETE FROM members;",
            "DELETE FROM hosts;", "DELETE FROM trips;", "DELETE FROM users;"));
    for (String query : deleteQueries) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.executeUpdate();
      }
    }
  }

  /**
   * Deletes a specific user by id.
   * @param id The String id of the user.
   * @throws SQLException Errors involving SQL queries.
   */
  protected void deleteUser(String id) throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM users WHERE id = ?;")) {
      prep.setString(1, id);
      prep.executeUpdate();
    }
  }

  /**
   * Updates the description for a specific trip.
   * @param tripId The id of the trip.
   * @param newMessage The new description to post.
   * @throws SQLException Errors involving SQL queries.
   */
  public void updateTripDescription(int tripId, String newMessage)
          throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.UPDATE_DESCRIPTION)) {
      prep.setString(1, newMessage);
      prep.setInt(2, tripId);
      prep.executeUpdate();
    }
  }

  /**
   * Updates the url for the user's profile picture.
   * @param userId The id of the user.
   * @param newUrl The new url to store.
   * @throws SQLException Errors involving SQL queries.
   */
  public void updateUserPicture(String userId, String newUrl)
          throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.UPDATE_PROFILE_PICTURE)) {
      prep.setString(1, newUrl);
      prep.setString(2, userId);
      prep.executeUpdate();
    }
  }
}
