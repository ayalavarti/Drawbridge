package edu.brown.cs.drawbridge.models;

import java.util.List;

/**
 * This class models a carpooling User.
 */
public class User {

  private String id;
  private String name;
  private String email;
  private List<Integer> hostingTripIds, memberTripIds, pendingTripIds;

  /**
   * Creates a new User object.
   *
   * @param id
   *          The User's id
   * @param name
   *          The name of the User
   * @param email
   *          The User's email address
   */
  public User(String id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  /**
   * Set the TripIds of the User.
   *
   * @param hostingTrips
   *          A List of ids of Trips that the User is hosting
   * @param memberTrips
   *          A list of ids of Trips that the User is a member of
   * @param pendingTrips
   *          A list of ids of Trips that the User has requested to join
   */
  public void setTrips(List<Integer> hostingTrips, List<Integer> memberTrips,
      List<Integer> pendingTrips) {
    this.hostingTripIds = hostingTrips;
    this.memberTripIds = memberTrips;
    this.pendingTripIds = pendingTrips;
  }

  /**
   * Get the id of the User.
   *
   * @return The User's id
   */
  public String getId() {
    return id;
  }

  /**
   * Get the name of the User.
   *
   * @return The User's name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the email of the User.
   *
   * @return The User's email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Get the Trips that the User is hosting.
   *
   * @return The ids of Trips that the User is hosting
   * @throws IllegalArgumentException
   *           If Trip information has not been processed by database
   */
  public List<Integer> getHostingTrips() {
    if (hostingTripIds == null) {
      throw new IllegalArgumentException(
          "ERROR: Trip information has not been initialized.");
    }
    return hostingTripIds;
  }

  /**
   * Get the Trips that the User is a member of.
   *
   * @return The ids of Trips that the User is a member of
   * @throws IllegalArgumentException
   *           If Trip information has not been processed by database
   */
  public List<Integer> getMemberTrips() {
    if (memberTripIds == null) {
      throw new IllegalArgumentException(
          "ERROR: Trip information has not been initialized.");
    }
    return memberTripIds;
  }

  /**
   * Get the Trips that the User has requested to join.
   *
   * @return The ids of Trips that the User has request to join
   * @throws IllegalArgumentException
   *           If Trip information has not been processed by database
   */
  public List<Integer> getPendingTrips() {
    if (pendingTripIds == null) {
      throw new IllegalArgumentException(
          "ERROR: Trip information has not been initialized.");
    }
    return pendingTripIds;
  }
}
