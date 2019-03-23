package edu.brown.cs.drawbridge.models;

import java.util.List;

/**
 * This class models a carpooling Trip.
 */
public class Trip {

  private int id;
  private String name;
  private double startingLatitude, startingLongitude, endingLatitude,
      endingLongitude;
  private int departureTime, eta;
  private int maxUsers;
  private double cost;
  private String phoneNumber, methodOfTransportation, comments;
  private String hostId;
  private List<String> memberIds, pendingIds;

  /**
   * Set the id and name of the Trip.
   *
   * @param tripId
   *          The id of the Trip
   * @param tripName
   *          The name of the Trip
   */
  private void setIdentification(int tripId, String tripName) {
    this.id = tripId;
    this.name = tripName;
  }

  /**
   * Set the starting and ending locations of the Trip.
   *
   * @param startLat
   *          The starting latitude
   * @param startLon
   *          The starting longitude
   * @param endLat
   *          The ending latitude
   * @param endLon
   *          The ending longitude
   */
  private void setLocations(double startLat, double startLon, double endLat,
      double endLon) {
    this.startingLatitude = startLat;
    this.startingLongitude = startLon;
    this.endingLatitude = endLat;
    this.endingLongitude = endLon;
  }

  /**
   * Set the start and end times of the Trip.
   *
   * @param startTime
   *          The epoch departure time
   * @param endTime
   *          The epoch estimated time of arrival
   */
  private void setTimes(int startTime, int endTime) {
    this.departureTime = startTime;
    this.eta = endTime;
  }

  /**
   * Set the host phone number, method of transportation, and comments of the
   * Trip.
   *
   * @param hostPhone
   *          The host's phone number
   * @param transportation
   *          The method of transportation
   * @param tripComments
   *          The Trip comments
   */
  private void setDetails(int maxSize, double tripCost, String hostPhone,
      String transportation, String tripComments) {
    this.maxUsers = maxSize;
    this.cost = tripCost;
    this.phoneNumber = hostPhone;
    this.methodOfTransportation = transportation;
    this.comments = tripComments;
  }

  /**
   * Set the host id, the ids of members in the Trip, and the ids of Users who
   * have requested to join the Trip.
   *
   * @param host
   *          The id of the host
   * @param members
   *          The ids of members in the Trip
   * @param pending
   *          The ids of Users who have requested to join the Trip
   */
  private void setUsers(String host, List<String> members,
      List<String> pending) {
    this.hostId = host;
    this.memberIds = members;
    this.pendingIds = pending;
  }

  /**
   * Get the id of the Trip.
   *
   * @return The Trip's id
   */
  public int getId() {
    return id;
  }

  /**
   * Get the name of the Trip.
   *
   * @return The Trip's name
   */
  public String getName() {
    return name;
  }

  /**
   * Get the starting latitude of the Trip.
   *
   * @return The starting latitude of the Trip
   */
  public double getStartingLatitude() {
    return startingLatitude;
  }

  /**
   * Get the starting longitude of the Trip.
   *
   * @return The starting longitude of the Trip
   */
  public double getStartingLongitude() {
    return startingLongitude;
  }

  /**
   * Get the ending latitude of the Trip.
   *
   * @return The ending latitude of the Trip
   */
  public double getEndingLatitude() {
    return endingLatitude;
  }

  /**
   * Get the ending longitude of the Trip.
   *
   * @return The ending longitude of the Trip
   */
  public double getEndingLongitude() {
    return endingLongitude;
  }

  /**
   * Get the departure time of the Trip.
   *
   * @return The departure time of the Trip
   */
  public int getDepartureTime() {
    return departureTime;
  }

  /**
   * Get the eta of the Trip.
   *
   * @return The eta of the Trip
   */
  public int getEta() {
    return eta;
  }

  /**
   * Get the maximum number of Users.
   *
   * @return The maximum number of Users
   */
  public int getMaxUsers() {
    return maxUsers;
  }

  /**
   * Get the cost of the Trip.
   *
   * @return The cost of the Trip
   */
  public double getCost() {
    return cost;
  }

  /**
   * Get the host's phone number.
   *
   * @return The host's phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Get the method of transportation.
   *
   * @return The method of transportation
   */
  public String getMethodOfTransportation() {
    return methodOfTransportation;
  }

  /**
   * Get the Trip comments.
   *
   * @return The Trip comments
   */
  public String getComments() {
    return comments;
  }

  /**
   * Get the id of the Trip's host.
   *
   * @return The id of the Trip's host
   */
  public String getHostId() {
    if (hostId == null) {
      throw new IllegalArgumentException(
          "ERROR: User information has not been initialized.");
    }
    return hostId;
  }

  /**
   * Get the ids of members in the Trip.
   *
   * @return A List of ids of members in the Trip.
   */
  public List<String> getMemberIds() {
    if (memberIds == null) {
      throw new IllegalArgumentException(
          "ERROR: User information has not been initialized.");
    }
    return memberIds;
  }

  /**
   * Get the ids of Users who have requested to join the Trip.
   *
   * @return A list of Users who have requested to join the Trip
   */
  public List<String> getPendingIds() {
    if (pendingIds == null) {
      throw new IllegalArgumentException(
          "ERROR: User information has not been initialized.");
    }
    return pendingIds;
  }

  /**
   * Get the current number of Users in the Trip.
   *
   * @return The current number of Users in the Trip
   */
  public int getCurrentSize() {
    if (memberIds == null) {
      throw new IllegalArgumentException(
          "ERROR: User information has not been initialized.");
    }
    return 1 + memberIds.size();
  }

  /**
   * Get the cost per User in the Trip.
   *
   * @return The cost per User in the Trip
   */
  public double getCostPerUser() {
    return cost / getCurrentSize();
  }

  public static class TripBuilder {

    public static IdentificationStep newTripBuilder() {
      return new TripSteps();
    }

    public static interface IdentificationStep {
      LocationStep addIdentification(int id, String name);
    }

    public static interface LocationStep {
      TimeStep addLocations(double startingLatitude, double startingLongitude,
          double endingLatitude, double endingLongitude);
    }

    public static interface TimeStep {
      DetailsStep addTimes(int departuretime, int eta);
    }

    public static interface DetailsStep {
      BuildStep addDetails(int maxUsers, double cost, String phoneNumber,
          String methodOfTransportation, String comments);
    }

    public static interface BuildStep {
      public Trip build();

      public Trip buildWithUsers(String hostId, List<String> memberids,
          List<String> pendingIds);
    }

    private static class TripSteps implements IdentificationStep, LocationStep,
        TimeStep, DetailsStep, BuildStep {
      // Identification
      private int id;
      private String name;
      // Location
      private double startingLatitude, startingLongitude, endingLatitude,
          endingLongitude;
      // Time
      private int departureTime, eta;
      // Details
      private int maxUsers;
      private double cost;
      private String phoneNumber, methodOfTransportation, comments;

      @Override
      public LocationStep addIdentification(int tripId, String tripName) {
        this.id = tripId;
        this.name = tripName;
        return this;
      }

      @Override
      public TimeStep addLocations(double startLat, double startLon,
          double endLat, double endLon) {
        this.startingLatitude = startLat;
        this.startingLongitude = startLon;
        this.endingLatitude = endLat;
        this.endingLongitude = endLon;
        return this;
      }

      @Override
      public DetailsStep addTimes(int startTime, int endTime) {
        this.departureTime = startTime;
        this.eta = endTime;
        return this;
      }

      @Override
      public BuildStep addDetails(int maxSize, double costOfTrip,
          String hostPhone, String transportation, String tripComments) {
        this.maxUsers = maxSize;
        this.cost = costOfTrip;
        this.phoneNumber = hostPhone;
        this.methodOfTransportation = transportation;
        this.comments = tripComments;
        return this;
      }

      @Override
      public Trip build() {
        Trip trip = new Trip();
        trip.setIdentification(id, name);
        trip.setLocations(startingLatitude, startingLongitude, endingLatitude,
            endingLongitude);
        trip.setTimes(departureTime, eta);
        trip.setDetails(maxUsers, cost, phoneNumber, methodOfTransportation,
            comments);
        return trip;
      }

      @Override
      public Trip buildWithUsers(String host, List<String> members,
          List<String> pending) {
        Trip trip = new Trip();
        trip.setIdentification(id, name);
        trip.setLocations(startingLatitude, startingLongitude, endingLatitude,
            endingLongitude);
        trip.setTimes(departureTime, eta);
        trip.setDetails(maxUsers, cost, phoneNumber, methodOfTransportation,
            comments);
        trip.setUsers(host, members, pending);
        return trip;
      }
    }
  }
}
