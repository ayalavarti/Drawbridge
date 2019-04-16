package edu.brown.cs.drawbridge.carpools;

import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.tripcomparators.ComparesSearchedTrips;
import edu.brown.cs.drawbridge.tripcomparators.CostComparator;
import edu.brown.cs.drawbridge.tripcomparators.HostComparator;
import edu.brown.cs.drawbridge.tripcomparators.MemberComparator;
import edu.brown.cs.drawbridge.tripcomparators.MultipleTripComparator;
import edu.brown.cs.drawbridge.tripcomparators.PendingComparator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * A class to search for valid paths from a starting to ending location.
 *
 * @author Jeffrey Zhu
 */
public class TripSearcher {

  private static final int CONNECTION_WAIT_TIME = 30;
  private static final int MAX_TRIPS_PER_PATH = 3;
  private static final int MAX_PATH_OPTIONS = 5;
  private static final List<ComparesSearchedTrips> COMPARATORS = Arrays
      .asList(new HostComparator(), new MemberComparator(),
          new PendingComparator(), new CostComparator());
  private static final Comparator<List<Trip>> TRIP_COMPARATOR
      = new MultipleTripComparator(COMPARATORS);
  private DatabaseQuery database;

  /**
   * Creates a new TripSearcher.
   *
   * @param database
   *     the database object to use for queries
   */
  public TripSearcher(DatabaseQuery database) {
    this.database = database;
  }

  private void setUser(String userId) {
    for (ComparesSearchedTrips comparator : COMPARATORS) {
      comparator.setUserId(userId);
    }
  }

  /**
   * Calculate the distance (km) from a Trip's ending location to a destination.
   *
   * @param trip
   *     The Trip to calculate destination distance from
   * @param endLat
   *     The latitude of the destination
   * @param endLon
   *     The longitude of the destination
   *
   * @return
   */
  private double distance(Trip trip, double endLat, double endLon) {
    final int earthRadius = 6371;
    double lat1 = Math.toRadians(trip.getEndingLatitude());
    double lat2 = Math.toRadians(trip.getEndingLongitude());
    double lon1 = Math.toRadians(endLat);
    double lon2 = Math.toRadians(endLon);
    double latDifference = lat2 - lat1;
    double lonDifference = lon2 - lon1;
    double latSquares = Math.sin(latDifference / 2) * Math
        .sin(latDifference / 2);
    double lonSquares = Math.sin(lonDifference / 2) * Math
        .sin(lonDifference / 2);
    double products = latSquares + lonSquares * Math.cos(lat1) * Math.cos(lat2);
    double radians = 2 * Math
        .atan2(Math.sqrt(products), Math.sqrt(1 - products));
    return earthRadius * radians;
  }

  /**
   * Return whether or not a Trip is within the required radius (km) of the
   * destination.
   *
   * @param trip
   *     A Trip that may be within the required radius of the destination
   * @param distanceRadius
   *     The required radius that the Trip must be within of the
   *     destination (km)
   * @param endLat
   *     The latitude of the destination
   * @param endLon
   *     The longitude of the destination
   *
   * @return True if the Trip is within the required radius of the destination.
   *     False otherwise
   */
  private boolean isWithinDestinationRadius(Trip trip, double distanceRadius,
      double endLat, double endLon) {
    return distance(trip, endLat, endLon) <= distanceRadius;
  }

  private List<Trip> unwrap(PathNode node) {
    List<Trip> trips = node.trips;
    trips.add(node.current);
    return trips;
  }

  /**
   * Search for valid paths from a starting location to an ending location.
   *
   * @param userId
   *     The id of the User that is searching
   * @param startLat
   *     The latitude of the starting location
   * @param startLon
   *     The longitude of the starting location
   * @param endLat
   *     The latitude of the ending location
   * @param endLon
   *     The longitude of the ending location
   * @param departureTime
   *     The epoch departure time
   * @param distanceRadius
   *     The maximum walking distance between Trips or between a Trip and
   *     the destination (km)
   * @param timeRadius
   *     The amount of time (seconds) that the Trip can leave within of the
   *     departure time
   *
   * @return A List of valid paths. Each path is a List of Trips.
   */
  public List<List<Trip>> searchWithId(String userId, double startLat,
      double startLon, double endLat, double endLon, long departureTime,
      double distanceRadius, long timeRadius) {
    List<List<Trip>> paths = search(userId, startLat, startLon, endLat, endLon,
        departureTime, distanceRadius, timeRadius);
    Collections.sort(paths, TRIP_COMPARATOR);
    return paths;
  }

  /**
   * Search for valid paths from a starting location to an ending location.
   *
   * @param startLat
   *     The latitude of the starting location
   * @param startLon
   *     The longitude of the starting location
   * @param endLat
   *     The latitude of the ending location
   * @param endLon
   *     The longitude of the ending location
   * @param departureTime
   *     The epoch departure time
   * @param distanceRadius
   *     The maximum walking distance between Trips or between a Trip and
   *     the destination (km)
   * @param timeRadius
   *     The amount of time (seconds) that the Trip can leave within of the
   *     departure time
   *
   * @return A List of valid paths. Each path is a List of Trips.
   */
  public List<List<Trip>> searchWithoutId(double startLat, double startLon,
      double endLat, double endLon, long departureTime, double distanceRadius,
      int timeRadius) {
    List<List<Trip>> paths = search("", startLat, startLon, endLat, endLon,
        departureTime, distanceRadius, timeRadius);
    Collections.sort(paths, new CostComparator());
    return paths;
  }

  /**
   * Search for valid paths from a starting location to an ending location.
   *
   * @param userId
   *     The id of the User that is searching
   * @param startLat
   *     The latitude of the starting location
   * @param startLon
   *     The longitude of the starting location
   * @param endLat
   *     The latitude of the ending location
   * @param endLon
   *     The longitude of the ending location
   * @param departureTime
   *     The epoch departure time
   * @param distanceRadius
   *     The maximum walking distance between Trips or between a Trip and
   *     the destination (km)
   * @param timeRadius
   *     The amount of time (seconds) that the Trip can leave within of the
   *     departure time
   *
   * @return A List of valid paths. Each path is a List of Trips.
   */
  private List<List<Trip>> search(String userId, double startLat,
      double startLon, double endLat, double endLon, long departureTime,
      double distanceRadius, long timeRadius) {
    // Set the user id for all comparators
    setUser(userId);

    // Set up List of paths found and Queue of nodes to visit
    List<List<Trip>> paths = new ArrayList<>();
    // Contains PathNodes with the distance-to-destination heuristic
    Queue<PathNode> toVisit = new PriorityQueue<>();
    // Contains a Set of visited Trips
    Set<Trip> visited = new HashSet<>();
    // Contains a Map from Trips to their total path weight (no heuristic)
    Map<Trip, Double> weights = new HashMap<>();
    try {
      // Create list of paths from starting location
      List<Trip> startingTrips = database
          .getConnectedTripsWithinTimeRadius(startLat, startLon, distanceRadius,
              departureTime, timeRadius);
      for (Trip trip : startingTrips) {
        // Add weight to weights HashMap
        weights.put(trip, trip.getTripDistance());
        // Add PathNode with distance-to-destination heuristic to toVisit Queue
        toVisit.add(new PathNode(trip, endLat, endLon, weights));
      }

      // Search for valid paths to destination
      while (!toVisit.isEmpty() && paths.size() <= MAX_PATH_OPTIONS) {
        PathNode visitingNode = toVisit.poll();
        Trip visitingTrip = visitingNode.current;
        if (visited.contains(visitingTrip)) {
          continue;
        } else {
          visited.add(visitingTrip);
        }

        if (isWithinDestinationRadius(visitingTrip, distanceRadius, endLat,
            endLon)) {
          paths.add(unwrap(visitingNode));
          continue;
        } else {
          if (visitingNode.trips.size() < MAX_TRIPS_PER_PATH - 1) {
            for (Trip nextTrip : database
                .getConnectedTripsAfterEta(visitingTrip.getEndingLatitude(),
                    visitingTrip.getEndingLongitude(), distanceRadius,
                    visitingTrip.getEta(), CONNECTION_WAIT_TIME)) {
              if (weights.containsKey(nextTrip) && weights.get(nextTrip)
                  < weights.get(visitingTrip) + visitingTrip
                  .distanceTo(nextTrip)) {
                continue;
              } else {
                weights.put(nextTrip, weights.get(visitingTrip) + visitingTrip
                    .distanceTo(nextTrip));
                toVisit.add(new PathNode(visitingNode, nextTrip, weights));
              }
            }
          }
        }
      }
    } catch (SQLException | MissingDataException e) {
      assert false;
      e.printStackTrace();
    }
    return paths;
  }

  /**
   * A Node of the graph that contains the path to the current Trip and its
   * total path weight.
   */
  private class PathNode implements Comparable<PathNode> {
    private List<Trip> trips; // Previous trips in the path
    private Trip current; // Current trip in the path
    private double totalWeight; // Total weight of the path with the heuristic

    // The ending latitude and longitude of the destination
    private double endLat, endLon;

    /**
     * Construct a new PathNode with a single Trip. Path weight uses the
     * distance heuristic.
     *
     * @param trip
     *     The Trip reached
     * @param endLat
     *     The latitude of the destination
     * @param endLon
     *     The longitude of the destination
     * @param weights
     *     A Map containing information about path weights
     */
    PathNode(Trip trip, double endLat, double endLon,
        Map<Trip, Double> weights) {
      trips = new LinkedList<>();
      current = trip;
      // Actual weight + heuristic
      totalWeight = weights.get(trip) + distance(trip, endLat, endLon);
      this.endLat = endLat;
      this.endLon = endLon;
    }

    /**
     * Construct a new PathNode by adding a new Trip to an existing PathNode.
     *
     * @param old
     *     The old PathNode
     * @param newTrip
     *     The new Trip reached
     * @param weights
     *     A Map containing information about path weights
     */
    PathNode(PathNode old, Trip newTrip, Map<Trip, Double> weights) {
      trips = old.trips;
      trips.add(old.current);
      current = newTrip;
      endLat = old.endLat;
      endLon = old.endLon;
      // Actual weight + heuristic
      totalWeight = weights.get(newTrip) + distance(newTrip, endLat, endLon);
    }

    @Override public int compareTo(PathNode o) {
      return Double.compare(totalWeight, o.totalWeight);
    }
  }
}
