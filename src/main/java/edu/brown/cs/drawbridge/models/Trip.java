package edu.brown.cs.drawbridge.models;

import java.util.List;

/**
 * This class models a carpooling Trip.
 */
public class Trip {

  private final int id;
  private final String name;
  private final double startingLatitude, startingLongitude, endingLatitude,
      endingLongitude;
  private final int departureTime, eta;
  private final int maxUsers;
  private final double cost;
  private final String phoneNumber, methodOfTransportation, comments;
  private final String hostId;
  private final List<String> memberIds, pendingIds;

  /**
   * Creates a new Trip object.
   *
   * @param builder
   *          An object that builds a Trip object
   */
  public Trip(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.startingLatitude = builder.startingLatitude;
    this.startingLongitude = builder.startingLongitude;
    this.endingLatitude = builder.endingLatitude;
    this.endingLongitude = builder.endingLongitude;
    this.departureTime = builder.departureTime;
    this.eta = builder.eta;
    this.maxUsers = builder.maxUsers;
    this.cost = builder.cost;
    this.phoneNumber = builder.phoneNumber;
    this.methodOfTransportation = builder.methodOfTransportation;
    this.comments = builder.comments;
    this.hostId = builder.hostId;
    this.memberIds = builder.memberIds;
    this.pendingIds = builder.pendingIds;
  }

  /**
   * A Builder is an object that contains data about a Trip and creates the
   * Trip.
   */
  public static class Builder {
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
     * Creates a new Builder object.
     *
     * @param id
     *          The Trip's id
     * @param name
     *          The Trip's name
     * @param startingLatitude
     *          The starting latitude of the Trip
     * @param startingLongitude
     *          The starting longitude of the Trip
     * @param endingLatitude
     *          The ending latitude of the Trip
     * @param endingLongitude
     *          The ending longitude of the Trip
     * @param departureTime
     *          The epoch departure time of the Trip
     * @param eta
     *          The epoch estimated time of arrival of the Trip
     * @param maxUsers
     *          The maximum number of Users allowed in the Trip
     * @param cost
     *          The cost of the Trip in US dollars
     * @param phoneNumber
     *          The phone number of the Trip's host User
     * @param methodOfTransportation
     *          The method of transportation of the Trip
     * @param comments
     *          Additional information about the Trip
     * @param hostId
     *          The id of the Trip's host User
     * @param memberIds
     *          A list of ids of confirmed members in the Trip
     * @param pendingIds
     *          A list of Users that have requested to join the Trip
     */
    public Builder(int id, String name, double startingLatitude,
        double startingLongitude, double endingLatitude, double endingLongitude,
        int departureTime, int eta, int maxUsers, double cost,
        String phoneNumber, String methodOfTransportation, String comments,
        String hostId, List<String> memberIds, List<String> pendingIds) {
      this.id = id;
      this.name = name;
      this.startingLatitude = startingLatitude;
      this.startingLongitude = startingLongitude;
      this.endingLatitude = endingLatitude;
      this.endingLongitude = endingLongitude;
      this.departureTime = departureTime;
      this.eta = eta;
      this.maxUsers = maxUsers;
      this.cost = cost;
      this.phoneNumber = phoneNumber;
      this.methodOfTransportation = methodOfTransportation;
      this.comments = comments;
      this.hostId = hostId;
      this.memberIds = memberIds;
      this.pendingIds = pendingIds;
    }

    /**
     * Creates a new Trip.
     *
     * @return A Trip object using the data in the Builder
     */
    public Trip build() {
      return new Trip(this);
    }
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
    return hostId;
  }

  /**
   * Get the ids of members in the Trip.
   *
   * @return A List of ids of members in the Trip.
   */
  public List<String> getMemberIds() {
    return memberIds;
  }

  /**
   * Get the ids of Users who have requested to join the Trip.
   *
   * @return A list of Users who have requested to join the Trip
   */
  public List<String> getPendingIds() {
    return pendingIds;
  }

  /**
   * Get the current number of Users in the Trip.
   *
   * @return The current number of Users in the Trip
   */
  public int getCurrentSize() {
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
}
