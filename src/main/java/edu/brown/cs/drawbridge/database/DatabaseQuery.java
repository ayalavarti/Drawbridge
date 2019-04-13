package edu.brown.cs.drawbridge.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.internal.bind.SqlDateTypeAdapter;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;

public class DatabaseQuery {

  private Connection conn;
  public static final User DUMMY_USER = new User("0", "Mary",
      "mary@gmail.com");

  public static final Trip DUMMY_TRIP = Trip.TripBuilder.newTripBuilder()
      .addIdentification(0, "Mary's Carpool").addLocations(41.608550, -72.656662, 41.827104, -71.399639)
      .addAddressNames("","").addTimes(1553487799, 1553494999).addDetails(7, 8.40, "555-867-5309",
                                                   "Uber", "We'll be meeting at the Ratty around this time, but maybe a bit later")
      .buildWithUsers("118428670975676923422", new ArrayList<>(), new ArrayList<>());

  /**
   * A constructor based on the String name of the database.
   *
   * @param db
   *          The name of the database.
   * @param username The username used to access the database.
   * @param password The password used to access the database.
   */
  public DatabaseQuery(String db, String username, String password) {
    // this line loads the driver manager class, and must be
    // present for everything else to work properly
    try {
      Class.forName("org.postgresql.Driver");
      String urlToDB = "jdbc:postgresql:" + db;
      conn = DriverManager.getConnection(urlToDB, username, password);
    } catch (ClassNotFoundException | SQLException e) {
      assert false;
      e.printStackTrace();
    }
  }

  /**
   * Finds the id of the user hosting the specified trip.
   *
   * @param tripId
   *          The int id of the trip.
   * @return The String id of the host.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public String getHostOnTrip(int tripId)
          throws SQLException, MissingDataException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_HOST_USER)) {
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
   *          The int id of the trip.
   * @return The List of String ids of the trip's members.
   * @throws SQLException Errors involving SQL queries.
   */
  public List<String> getMembersOnTrip(int tripId) throws SQLException {
    List<String> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_MEMBER_USERS)) {
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
   *          The int id of the trip.
   * @return The List of String ids of all requesting users for that trip.
   * @throws SQLException Errors involving SQL queries.
   */
  public List<String> getRequestsOnTrip(int tripId) throws SQLException {
    List<String> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_REQUEST_USERS)) {
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
   *          The String id of the user.
   * @return The List of Integer ids of all trips hosted by the user.
   * @throws SQLException Errors involving SQL queries.
   */
  public List<Integer> getHostTripsWithUser(String userId) throws SQLException {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_HOST_TRIPS)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        while (rs.next()) {
          results.add(rs.getInt(1));
        }
      }
    }
    return results;
  }

//  /**
//   * Finds the ids of all the trips that the specified user is a confirmed
//   * member of.
//   *
//   * @param userId
//   *          The String id of the user.
//   * @return The List of Integer ids of all trips that the user is a member of.
//   * NOTE: Group trips are NOT guaranteed to be in chronological order.
//   * @throws SQLException Errors involving SQL queries.
//   */
//  public List<List<Integer>> getMemberTripsWithUser(String userId)
//          throws SQLException {
//    List<List<Integer>> results = new ArrayList<>();
//    try (PreparedStatement prep = conn.prepareStatement(
//            QueryStrings.FIND_MEMBER_TRIPS)) {
//      prep.setString(1, userId);
//      try (ResultSet rs = prep.executeQuery()) {
//        int lastGroupId = -1;
//        List<Integer> tripGroup = new ArrayList<>();
//        while (rs.next()) {
//          if (lastGroupId == -1) { //first group
//            tripGroup.add(rs.getInt(1));
//            lastGroupId = rs.getInt(2);
//          } else if (lastGroupId != rs.getInt(2)) { //new group
//            results.add(tripGroup);
//            tripGroup = new ArrayList<>();
//            tripGroup.add(rs.getInt(1));
//            lastGroupId = rs.getInt(2);
//          } else { //same group
//            tripGroup.add(rs.getInt(1));
//          }
//        }
//        if (!tripGroup.isEmpty()) {
//          results.add(tripGroup);
//        }
//      }
//    }
//    return results;
//  }
//
//  /**
//   * Finds the ids of all the trips that the specified user is requesting to
//   * join.
//   *
//   * @param userId
//   *          The String id of the user.
//   * @return The List of Integer ids of all trips that the user is requesting to
//   *         join.
//   * NOTE: Group trips are NOT guaranteed to be in chronological order.
//   * @throws SQLException Errors involving SQL queries.
//   */
//  public List<List<Integer>> getRequestTripsWithUser(String userId)
//          throws SQLException {
//    List<List<Integer>> results = new ArrayList<>();
//    try (PreparedStatement prep = conn.prepareStatement(
//            QueryStrings.FIND_REQUEST_TRIPS)) {
//      prep.setString(1, userId);
//      try (ResultSet rs = prep.executeQuery()) {
//        int lastGroupId = -1;
//        List<Integer> tripGroup = new ArrayList<>();
//        while (rs.next()) {
//          if (lastGroupId == -1) { //first group
//            tripGroup.add(rs.getInt(1));
//            lastGroupId = rs.getInt(2);
//          } else if (lastGroupId != rs.getInt(2)) { //new group
//            results.add(tripGroup);
//            tripGroup = new ArrayList<>();
//            tripGroup.add(rs.getInt(1));
//            lastGroupId = rs.getInt(2);
//          } else { //same group
//            tripGroup.add(rs.getInt(1));
//          }
//        }
//        if (!tripGroup.isEmpty()) {
//          results.add(tripGroup);
//        }
//      }
//    }
//    return results;
//  }

  /**
   * Finds the ids of all the trips that the specified user is a confirmed
   * member of.
   *
   * @param userId
   *          The String id of the user.
   * @return The List of Integer ids of all trips that the user is a member of.
   * @throws SQLException Errors involving SQL queries.
   */
  public List<Integer> getMemberTripsWithUser(String userId)
          throws SQLException {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_MEMBER_TRIPS)) {
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
   *          The String id of the user.
   * @return The List of Integer ids of all trips that the user is requesting to
   *         join.
   * @throws SQLException Errors involving SQL queries.
   */
  public List<Integer> getRequestTripsWithUser(String userId)
          throws SQLException {
    List<Integer> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_REQUEST_TRIPS)) {
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
   *          The String id of the user.
   * @return The User object with the specified id.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public User getUserById(String userId)
          throws SQLException, MissingDataException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_USER_BY_ID)) {
      prep.setString(1, userId);
      try (ResultSet rs = prep.executeQuery()) {
        if (rs.next()) {
          User u = new User(userId, rs.getString(1), rs.getString(2));
          u.setTrips(getHostTripsWithUser(userId), getMemberTripsWithUser(userId),
                  getRequestTripsWithUser(userId));
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
   *          The int id of the trip.
   * @return The Trip object with the specified id.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public Trip getTripById(int tripId) throws SQLException, MissingDataException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_TRIP_BY_ID)) {
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
    }
    throw new MissingDataException("Trip does not exist");
  }

  /**
   * Adds a new User to the database.
   *
   * @param user
   *          The User object to add.
   * @throws SQLException Errors involving SQL queries.
   */
  public void addUser(User user) throws SQLException {
    try {
      getUserById(user.getId());
    } catch (MissingDataException e) {
      try (PreparedStatement prep = conn.prepareStatement(
              QueryStrings.INSERT_USER)) {
        prep.setString(1, user.getId());
        prep.setString(2, user.getName());
        prep.setString(3, user.getEmail());
        prep.addBatch();
        prep.executeUpdate();
      }
    }
  }

  /**
   * Adds a new Trip to the databse.
   *
   * @param trip
   *          The Trip object to add.
   * @param hostId
   *          The String id of the user hosting the trip.
   * @return The database's id of the inserted trip.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public int createTrip(Trip trip, String hostId)
          throws SQLException, MissingDataException {
    int tripId = -1;
    getUserById(hostId);
    //insert into trip
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.INSERT_TRIP, Statement.RETURN_GENERATED_KEYS)) {
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
    }
    //insert into host
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.INSERT_HOST)) {
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
   *          The int id of the trip.
   * @throws SQLException Errors involving SQL queries.
   */
  public void deleteTripManually(int tripId) throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.REMOVE_TRIP_BY_ID)) {
      prep.setInt(1, tripId);
      prep.executeUpdate();
    }
  }

  /**
   * Deletes all trips that have already departed from the database.
   * @throws SQLException Errors involving SQL queries.
   */
  public void deleteExpiredTrips() throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.REMOVE_TRIPS_BY_TIME)) {
      prep.executeUpdate();
    }
  }

  /**
   * Finds all of the trips that are graphically connected to the input trip.
   * That is, trips that depart near the given destination and that depart after
   * the given departure time within a specific time frame.
   *
   * @param lastLat The double ending latitude of the previous trip.
   * @param lastLon The double ending longitude of the previous trip.
   * @param walkRadius
   *          The buffer for finding reasonably distanced trips.
   * @param start The int beginning of the time window in epoch time.
   * @param end The int end of the time window in epoch time.
   * @return A List of all the trips connected to the given trip.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public List<Trip> searchTripsByTimeWindow(
          double lastLat, double lastLon, double walkRadius,
          int start, int end) throws SQLException, MissingDataException {
    List<Trip> results = new ArrayList<>();
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.FIND_CONNECTED_TRIPS)) {
      prep.setDouble(1, lastLat);
      prep.setDouble(2, lastLat);
      prep.setDouble(3, lastLon);
      prep.setDouble(4, walkRadius);
      prep.setInt(5, start);
      prep.setInt(6, end);
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
   * @param lastLat The double ending latitude of the previous trip.
   * @param lastLon The double ending longitude of the previous trip.
   * @param walkRadius
   *          The buffer for finding reasonably distanced trips.
   * @param lastEta The int expected arrival time of the last trip.
   * @param timeBuffer
   *          The buffer for finding reasonably timed trips.
   * @return A List of all the trips connected to the given trip.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public List<Trip> getConnectedTripsAfterEta(
          double lastLat, double lastLon, double walkRadius,
          int lastEta, int timeBuffer)
          throws SQLException, MissingDataException {
    return searchTripsByTimeWindow(
            lastLat, lastLon, walkRadius, lastEta, lastEta + timeBuffer);
  }

  /**
   * Finds all of the trips that match the search criteria based on departure.
   *
   * @param lat The double latitude of departure.
   * @param lat The double longitude of departure.
   * @param walkRadius
   *          The buffer for finding reasonably distanced trips.
   * @param departure The int time of departure.
   * @param timeBuffer
   *          The buffer for finding reasonably timed trips.
   * @return A List of all the trips leaving around the specified location and
   * time.
   * @throws SQLException Errors involving SQL queries.
   * @throws MissingDataException Errors involving the database's contents.
   */
  public List<Trip> getConnectedTripsWithinTimeRadius(
          double lat, double lon, double walkRadius,
          int departure, int timeBuffer)
          throws SQLException, MissingDataException {
    return searchTripsByTimeWindow(
            lat, lon, walkRadius, departure - timeBuffer, departure + timeBuffer);
  }

//  /**
//   * Inserts a request relation into the database.
//   *
//   * @param tripIds
//   *          The list of Integer ids of the trips being requested.
//   * @param userId
//   *          The String id of the user requesting to join the trip.
//   * @return True if the request was processed successfully. False otherwise.
//   */
//  public boolean request(List<Integer> tripIds, String userId) {
//    int groupId = 0;
//    int[] primeSet = {2, 3, 5};
//    for (int i = 0; i < tripIds.size(); i++) {
//      groupId *= Math.pow(primeSet[i], tripIds.get(i));
//    }
//    for (int i = 0; i < tripIds.size(); i++) {
//      try (PreparedStatement prep = conn.prepareStatement(
//              QueryStrings.INSERT_REQUEST)) {
//        prep.setInt(1, tripIds.get(i));
//        prep.setString(2, userId);
//        prep.setInt(3, groupId);
//        prep.addBatch();
//        prep.executeUpdate();
//      } catch (SQLException e) {
//        return false;
//      }
//    }
//    return true;
//  }
//
//  /**
//   * Approves a request by inserting a member relation while removing its
//   * corresponding request relation.
//   *
//   * @param tripId
//   *          The int id of the trip being requested.
//   * @param userId
//   *          The String id of the user requesting to join the trip.
//   * @return True if the approval was processed successfully. False otherwise.
//   */
//  public boolean approve(int tripId, String userId) {
//    //delete from requests
//    int groupId = 0;
//    try (PreparedStatement prep = conn.prepareStatement(
//            QueryStrings.REMOVE_REQUEST)) {
//      prep.setInt(1, tripId);
//      prep.setString(2,userId);
//      try (ResultSet rs = prep.executeQuery()) {
//        while (rs.next()) {
//          groupId = rs.getInt(1);
//        }
//      }
//    } catch (SQLException e) {
//      return false;
//    }
//    //insert into members
//    try (PreparedStatement prep = conn.prepareStatement(
//            QueryStrings.INSERT_MEMBER)) {
//      prep.setInt(1, tripId);
//      prep.setString(2,userId);
//      prep.setInt(3, groupId);
//      prep.addBatch();
//      prep.executeUpdate();
//      return true;
//    } catch (SQLException e) {
//      return false;
//    }
//  }
//
//  /**
//   * Rejects a request by removing its relation from the database.
//   *
//   * @param tripId
//   *          The int id of the trip being requested.
//   * @param userId
//   *          The String id of the user requesting to join the trip.
//   * @return True if the rejection was processed successfully. False otherwise.
//   */
//  public boolean reject(int tripId, String userId) {
//    int groupId = 0;
//    try (PreparedStatement prep = conn.prepareStatement(
//            QueryStrings.REMOVE_REQUEST)) {
//      prep.setInt(1, tripId);
//      prep.setString(2,userId);
//      try (ResultSet rs = prep.executeQuery()) {
//        while (rs.next()) {
//          groupId = rs.getInt(1);
//        }
//      }
//    } catch (SQLException e) {
//      return false;
//    }
//    return deleteGroup(groupId);
//  }
//
//  /**
//   * Kicks a member by removing its relation from the database.
//   *
//   * @param tripId
//   *          The int id of the trip.
//   * @param userId
//   *          The String id of the user being kicked from the trip.
//   * @return True if the kick was processed successfully. False otherwise.
//   */
//  public boolean kick(int tripId, String userId) {
//    int groupId = 0;
//    try (PreparedStatement prep = conn.prepareStatement(
//            QueryStrings.REMOVE_MEMBER)) {
//      prep.setInt(1, tripId);
//      prep.setString(2,userId);
//      try (ResultSet rs = prep.executeQuery()) {
//        while (rs.next()) {
//          groupId = rs.getInt(1);
//        }
//      }
//    } catch (SQLException e) {
//      return false;
//    }
//    return deleteGroup(groupId);
//  }

  /**
   * Inserts a request relation into the database.
   *
   * @param tripId
   *          The int id of the trip being requested.
   * @param userId
   *          The String id of the user requesting to join the trip.
   * @throws SQLException Errors involving SQL queries.
   */
  public void request(int tripId, String userId) throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.INSERT_REQUEST)) {
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
   *          The int id of the trip being requested.
   * @param userId
   *          The String id of the user requesting to join the trip.
   * @throws SQLException Errors involving SQL queries.
   */
  public void approve(int tripId, String userId) throws SQLException {
    //insert into members
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.INSERT_MEMBER)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.addBatch();
      prep.executeUpdate();
    }
    //delete from requests
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.REMOVE_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
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
   *          The int id of the trip being requested.
   * @param userId
   *          The String id of the user requesting to join the trip.
   * @throws SQLException Errors involving SQL queries.
   */
  public void reject(int tripId, String userId) throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.REMOVE_REQUEST)) {
      prep.setInt(1, tripId);
      prep.setString(2, userId);
      prep.executeUpdate();
    }
  }

  /**
   * Kicks a member by removing its relation from the database.
   *
   * @param tripId
   *          The int id of the trip.
   * @param userId
   *          The String id of the user being kicked from the trip.
   * @throws SQLException Errors involving SQL queries.
   */
  public void kick(int tripId, String userId) throws SQLException {
    try (PreparedStatement prep = conn.prepareStatement(
            QueryStrings.REMOVE_MEMBER)) {
      prep.setInt(1, tripId);
      prep.setString(2,userId);
      prep.executeUpdate();
    }
  }

//  private boolean deleteGroup(int groupId) {
//    if (groupId != 0) {
//      try (PreparedStatement prep = conn.prepareStatement(
//              QueryStrings.REMOVE_MEMBER_GROUP)) {
//        prep.setInt(1, groupId);
//        prep.executeUpdate();
//      } catch (SQLException e) {
//        return false;
//      }
//      try (PreparedStatement prep = conn.prepareStatement(
//              QueryStrings.REMOVE_REQUEST_GROUP)) {
//        prep.setInt(1, groupId);
//        prep.executeUpdate();
//      } catch (SQLException e) {
//        return false;
//      }
//    }
//    return true;
//  }

  /**
   * Clears all data from the database.
   * @throws SQLException Errors involving SQL queries.
   */
  public void clearData() throws SQLException {
    List<String> deleteQueries = new ArrayList<String>(Arrays.asList(
            "DELETE FROM requests;",
            "DELETE FROM members;",
            "DELETE FROM hosts;",
            "DELETE FROM trips;",
            "DELETE FROM users;"));
    for (String query : deleteQueries) {
      try (PreparedStatement prep = conn.prepareStatement(query)) {
        prep.executeUpdate();
      }
    }
  }
}
