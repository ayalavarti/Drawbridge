package edu.brown.cs.drawbridge.carpools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import edu.brown.cs.drawbridge.tripcomparators.TimeComparator;

/**
 * A class used to interact with the GUI handlers.
 */
public final class Carpools {

  private static final long SECONDS_PER_MINUTE = 60;
  private static final double WALKING_SPEED = 0.084; // Kilometers per minute
  private final TripSearcher tripSearcher;
  private final Comparator<Trip> timeComparator = new TimeComparator();
  private DatabaseQuery database;

  /**
   * Create a new Carpools object.
   *
   * @param dbName
   *          name of the database to connect to
   * @param dbUser
   *          username to connect with
   * @param dbPass
   *          password for dbUser
   *
   * @throws ClassNotFoundException
   *           when can't connect to db
   * @throws SQLException
   *           when database throws an errors
   */
  public Carpools(String dbName, String dbUser, String dbPass)
      throws ClassNotFoundException, SQLException {
    database = new DatabaseQuery(dbName, dbUser, dbPass);
    tripSearcher = new TripSearcher(database);
  }

  /**
   * Add a User to the database if it does not already exist.
   *
   * @param user
   *          The User to add to the database
   * @return True if the User is added to the database. False if the User
   *         already exists in the database.
   * @throws SQLException
   *           If the SQL query is invalid.
   */
  public boolean addUser(User user) throws SQLException {
    try {
      database.getUserById(user.getId());
      return false;
    } catch (MissingDataException e) {
      database.addUser(user);
      return true;
    }
  }

  /**
   * Search for valid paths from a starting location to an ending location given
   * a User id.
   *
   * @param userId
   *          The id of the User that is searching
   * @param startLat
   *          The latitude of the starting location
   * @param startLon
   *          The longitude of the starting location
   * @param endLat
   *          The latitude of the ending location
   * @param endLon
   *          The longitude of the ending location
   * @param departureTime
   *          The epoch departure time
   * @param walkingTime
   *          The maximum walking time between Trips or between a Trip and the
   *          destination (minutes)
   * @param timeRadius
   *          The amount of time (minutes) that the Trip can leave within of the
   *          departure time
   *
   * @return A List of valid paths. Each path is a List of Trips.
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public List<List<Trip>> searchWithId(String userId, double startLat,
      double startLon, double endLat, double endLon, long departureTime,
      long walkingTime, double timeRadius)
      throws SQLException, MissingDataException {
    return tripSearcher.searchWithId(userId, startLat, startLon, endLat, endLon,
        departureTime, walkingTime * WALKING_SPEED,
        timeRadius * SECONDS_PER_MINUTE);
  }

  /**
   * Search for valid paths from a starting location to an ending location
   * without a User id.
   *
   * @param startLat
   *          The latitude of the starting location
   * @param startLon
   *          The longitude of the starting location
   * @param endLat
   *          The latitude of the ending location
   * @param endLon
   *          The longitude of the ending location
   * @param departureTime
   *          The epoch departure time
   * @param walkingTime
   *          The maximum walking time between Trips or between a Trip and the
   *          destination (minutes)
   * @param timeRadius
   *          The amount of time (minutes) that the Trip can leave within of the
   *          departure time
   *
   * @return A List of valid paths. Each path is a List of Trips.
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public List<List<Trip>> searchWithoutId(double startLat, double startLon,
      double endLat, double endLon, long departureTime, double walkingTime,
      long timeRadius) throws SQLException, MissingDataException {
    return tripSearcher.searchWithoutId(startLat, startLon, endLat, endLon,
        departureTime, walkingTime * WALKING_SPEED,
        timeRadius * SECONDS_PER_MINUTE);
  }

  /**
   * Get a List of List of all Users in a Trip, including the host, members, and
   * pending.
   *
   * @param tripId
   *          The id of the Trip
   *
   * @return A list of size 3. Each element is a List containing the host,
   *         members, or pending in a Trip in that order.
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public List<List<User>> getUsers(int tripId)
      throws SQLException, MissingDataException {

    List<User> host = new ArrayList<>();
    host.add(database.getUserById(database.getHostOnTrip(tripId)));

    List<User> members = new ArrayList<>();
    for (String memberId : database.getMembersOnTrip(tripId)) {
      members.add(database.getUserById(memberId));
    }

    List<User> requesting = new ArrayList<>();
    for (String pendingId : database.getRequestsOnTrip(tripId)) {
      requesting.add(database.getUserById(pendingId));
    }
    List<List<User>> allUsers = new ArrayList<>();
    allUsers.add(host);
    allUsers.add(members);
    allUsers.add(requesting);
    return allUsers;
  }

  /**
   * Get a List of List of hosting, member, and pending Trips.
   *
   * @param userId
   *          The id of the User
   *
   * @return A list of size 3. Each element is a List of all Trips that a User
   *         hosting, member, or pending in that order.
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public List<List<Trip>> getTrips(String userId)
      throws SQLException, MissingDataException {
    List<Trip> hostingTrips = new ArrayList<>();
    for (int tripId : database.getHostTripsWithUser(userId)) {
      hostingTrips.add(database.getTripById(tripId));
    }
    Collections.sort(hostingTrips, timeComparator);
    List<Trip> memberTrips = new ArrayList<>();
    for (int tripId : database.getMemberTripsWithUser(userId)) {
      memberTrips.add(database.getTripById(tripId));
    }
    Collections.sort(memberTrips, timeComparator);
    List<Trip> pendingTrips = new ArrayList<>();
    for (int tripId : database.getRequestTripsWithUser(userId)) {
      pendingTrips.add(database.getTripById(tripId));
    }
    Collections.sort(pendingTrips, timeComparator);

    List<List<Trip>> allTrips = new ArrayList<>();
    allTrips.add(hostingTrips);
    allTrips.add(memberTrips);
    allTrips.add(pendingTrips);
    return allTrips;
  }

  /**
   * Request to join a Trip given a User. Return whether or not the request is
   * successful.
   *
   * @param tripId
   *          The id of the Trip
   * @param userId
   *          The id of the User
   *
   * @return True if the request was successful. False if it was unsuccessful
   *         (the User is already host, member, pending, or Trip is full)
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public boolean joinTrip(int tripId, String userId)
      throws SQLException, MissingDataException {
    Trip toJoin = database.getTripById(tripId);
    if (toJoin.getHostId().equals(userId)
        || toJoin.getMemberIds().contains(userId)
        || toJoin.getPendingIds().contains(userId)
        || toJoin.getCurrentSize() >= toJoin.getMaxUsers()) {
      return false;
    } else {
      database.request(tripId, userId);
      return true;
    }
  }

  /**
   * Create a Trip with a new host.
   *
   * @param trip
   *          The new Trip to create
   * @param hostId
   *          <<<<<<< HEAD The id of the new host
   *
   *          ======= The id of the new host
   * @return The id of the new created trip >>>>>>>
   *         0098abd148d443b06657231536a4107c8f74607c
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public int createTrip(Trip trip, String hostId)
      throws SQLException, MissingDataException {
    return database.createTrip(trip, hostId);
  }

  /**
   * Get a Trip given a tripId.
   *
   * @param tripId
   *          The id of a Trip
   *
   * @return The Trip
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If the trip does not exist
   */
  public Trip getTrip(int tripId) throws SQLException, MissingDataException {
    return database.getTripById(tripId);
  }

  /**
   * Remove a member from a Trip (both from requests and from members).
   *
   * @param tripId
   *          The id of the Trip
   * @param userId
   *          The id of the User
   *
   * @return True if the removal was successful. False if it was unsuccessful
   *         (the User is not a member)
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   */
  public boolean leaveTrip(int tripId, String userId) throws SQLException {
    if (database.getMembersOnTrip(tripId).contains(userId)) {
      database.kick(tripId, userId);
      return true;
    }
    if (database.getRequestsOnTrip(tripId).contains(userId)) {
      database.reject(tripId, userId);
      return true;
    }

    return false;
  }

  /**
   * Delete a trip.
   *
   * @param tripId
   *          The id of the Trip
   * @param userId
   *          The id of the User
   *
   * @return True if the deletion was successful. False if it was unsuccessful
   *         (the User is not the host)
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public boolean deleteTrip(int tripId, String userId)
      throws SQLException, MissingDataException {
    if (database.getHostOnTrip(tripId).equals(userId)) {
      database.deleteTripManually(tripId);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Approve a pender's request.
   *
   * @param tripId
   *          The id of the Trip
   * @param approver
   *          The host id
   * @param pender
   *          The id of the user to approve
   *
   * @return True if the approval was successful. False if it was unsuccessful
   *         (the approver is not the host, the pender is not pending, or the
   *         Trip is full)
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public boolean approveRequest(int tripId, String approver, String pender)
      throws SQLException, MissingDataException {
    Trip trip = database.getTripById(tripId);
    if (trip.getHostId().equals(approver)
        && trip.getPendingIds().contains(pender)
        && trip.getCurrentSize() < trip.getMaxUsers()) {
      database.approve(tripId, pender);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Reject a pender's request.
   *
   * @param tripId
   *          The id of the Trip
   * @param rejector
   *          The host id
   * @param pender
   *          The id of the user to reject
   *
   * @return True if the rejection was successful. False if it was unsuccessful
   *         (the rejector is not the host or if the pender is not pending)
   *
   * @throws SQLException
   *           If the SQL query is invalid.
   * @throws MissingDataException
   *           If database is missing information
   */
  public boolean rejectRequest(int tripId, String rejector, String pender)
      throws SQLException, MissingDataException {
    Trip trip = database.getTripById(tripId);
    if (trip.getHostId().equals(rejector)
        && trip.getPendingIds().contains(pender)) {
      database.reject(tripId, pender);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Gets a user given the user's id.
   * @param uid The user id.
   * @return The user object associated with the id.
   * @throws SQLException when the database has an error
   * @throws MissingDataException when the user doesn't exist.
   */
  public User getUserById(String uid)
          throws SQLException, MissingDataException {
    return database.getUserById(uid);
  }

  DatabaseQuery getDatabase() {
    return database;
  }
}
