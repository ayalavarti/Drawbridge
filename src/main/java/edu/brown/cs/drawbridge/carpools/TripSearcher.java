package edu.brown.cs.drawbridge.carpools;

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

import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.tripcomparators.ComparesSearchedTrips;
import edu.brown.cs.drawbridge.tripcomparators.CostComparator;
import edu.brown.cs.drawbridge.tripcomparators.HostComparator;
import edu.brown.cs.drawbridge.tripcomparators.MemberComparator;
import edu.brown.cs.drawbridge.tripcomparators.MultipleTripComparator;
import edu.brown.cs.drawbridge.tripcomparators.PendingComparator;

public class TripSearcher {

  private static final int CONNECTION_WAIT_TIME = 30;
  private static final int MAX_TRIPS_PER_PATH = 3;
  private static final int MAX_PATH_OPTIONS = 5;
  private DatabaseQuery database;

  private static final List<ComparesSearchedTrips> COMPARATORS = Arrays.asList(
      new HostComparator(), new MemberComparator(), new PendingComparator(),
      new CostComparator());
  private static final Comparator<List<Trip>> TRIP_COMPARATOR = new MultipleTripComparator(
      COMPARATORS);

  public TripSearcher() throws ClassNotFoundException, SQLException {
    database = new DatabaseQuery("//localhost/carpools", "", "");
  }

  private void setUser(String userId) {
    for (ComparesSearchedTrips comparator : COMPARATORS) {
      comparator.setUserId(userId);
    }
  }

  private class PathNode implements Comparable<PathNode> {
    private List<Trip> trips; // Previous trips in the path
    private Trip current; // Current trip in the path
    private double totalWeight; // Total weight of the path with the heuristic

    // The ending latitude and longitude of the destination
    private double endLat, endLon;

    PathNode(Trip trip, double endLat, double endLon,
        Map<Trip, Double> weights) {
      trips = new LinkedList<Trip>();
      current = trip;
      // Actual weight + heuristic
      totalWeight = weights.get(trip) + distance(trip, endLat, endLon);
      this.endLat = endLat;
      this.endLon = endLon;
    }

    PathNode(PathNode old, Trip newTrip, Map<Trip, Double> weights) {
      trips = old.trips;
      trips.add(old.current);
      current = newTrip;
      endLat = old.endLat;
      endLon = old.endLon;
      // Actual weight + heuristic
      totalWeight = weights.get(newTrip) + distance(newTrip, endLat, endLon);
    }

    @Override
    public int compareTo(PathNode o) {
      return Double.compare(this.totalWeight, o.totalWeight);
    }
  }

  private double distance(Trip trip, double endLat, double endLon) {
    double lat1 = Math.toRadians(trip.getEndingLatitude());
    double lat2 = Math.toRadians(trip.getEndingLongitude());
    double lon1 = Math.toRadians(endLat);
    double lon2 = Math.toRadians(endLon);
    double latDifference = lat2 - lat1;
    double lonDifference = lon2 - lon1;
    double latSquares = Math.sin(latDifference / 2)
        * Math.sin(latDifference / 2);
    double lonSquares = Math.sin(lonDifference / 2)
        * Math.sin(lonDifference / 2);
    double products = latSquares + lonSquares * Math.cos(lat1) * Math.cos(lat2);
    return Math.toDegrees(
        2 * Math.atan2(Math.sqrt(products), Math.sqrt(1 - products)));
  }

  private boolean isWithinDestinationRadius(Trip trip, double distanceRadius,
      double endLat, double endLon) {
    return distance(trip, endLat, endLon) <= distanceRadius;
  }

  private List<Trip> unwrap(PathNode node) {
    List<Trip> trips = node.trips;
    trips.add(node.current);
    return trips;
  }

  public List<List<Trip>> searchWithId(String userId, double startLat,
      double startLon, double endLat, double endLon, int departureTime,
      double distanceRadius, int timeRadius) {
    List<List<Trip>> paths = search(userId, startLat, startLon, endLat, endLon,
        departureTime, distanceRadius, timeRadius);
    Collections.sort(paths, TRIP_COMPARATOR);
    return paths;
  }

  public List<List<Trip>> searchWithoutId(double startLat, double startLon,
      double endLat, double endLon, int departureTime, double distanceRadius,
      int timeRadius) {
    List<List<Trip>> paths = search("", startLat, startLon, endLat, endLon,
        departureTime, distanceRadius, timeRadius);
    Collections.sort(paths, new CostComparator());
    return paths;
  }

  private List<List<Trip>> search(String userId, double startLat,
      double startLon, double endLat, double endLon, int departureTime,
      double distanceRadius, int timeRadius) {

    // Set the user id for all comparators
    if (userId.equals("")) {
      setUser(userId);
    }

    // Set up List of paths found and Queue of nodes to visit
    List<List<Trip>> paths = new ArrayList<List<Trip>>();
    // Contains PathNodes with the distance-to-destination heuristic
    Queue<PathNode> toVisit = new PriorityQueue<PathNode>();
    // Contains a Set of visited Trips
    Set<Trip> visited = new HashSet<Trip>();
    // Contains a Map from Trips to their total path weight (no heuristic)
    Map<Trip, Double> weights = new HashMap<Trip, Double>();

    // Create list of paths from starting location
    List<Trip> startingTrips = database.getConnectedTripsWithinTimeRadius(
        startLat, startLon, distanceRadius, departureTime, timeRadius);
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
          for (Trip nextTrip : database.getConnectedTripsAfterEta(
              visitingTrip.getEndingLatitude(),
              visitingTrip.getEndingLongitude(), distanceRadius,
              visitingTrip.getEta(), CONNECTION_WAIT_TIME)) {
            if (weights.containsKey(nextTrip)
                && weights.get(nextTrip) < weights.get(visitingTrip)
                    + visitingTrip.distanceTo(nextTrip)) {
              continue;
            } else {
              weights.put(nextTrip, weights.get(visitingTrip)
                  + visitingTrip.distanceTo(nextTrip));
              toVisit.add(new PathNode(visitingNode, nextTrip, weights));
            }
          }
        }
      }
    }
    return paths;
  }
}
