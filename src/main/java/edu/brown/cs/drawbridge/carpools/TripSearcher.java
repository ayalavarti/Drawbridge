package edu.brown.cs.drawbridge.carpools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
      comparator.setId(userId);
    }
  }

  private class PathNode {
    private List<Trip> trips;
    private Trip current;

    PathNode(Trip trip) {
      current = trip;
    }

    public void addTrip(Trip trip) {
      trips.add(current);
      current = trip;
    }
  }

  private boolean isWithinDestinationRadius(Trip trip, double distanceRadius,
      double endLat, double endLon) {
    return Math.abs(trip.getEndingLatitude() - endLat) <= distanceRadius
        && Math.abs(trip.getEndingLongitude() - endLon) <= distanceRadius;
  }

  private List<Trip> unwrap(PathNode node) {
    List<Trip> trips = node.trips;
    trips.add(node.current);
    return trips;
  }

  public List<List<Trip>> search(String userId, double startLat,
      double startLon, double endLat, double endLon, int departureTime,
      double distanceRadius, int timeRadius) {

    // Set the user id for all comparators
    setUser(userId);

    List<List<Trip>> paths = new ArrayList<List<Trip>>();

    // Create list of paths from starting location
    List<Trip> startingTrips = database.getConnectedTrips(startLat, startLon,
        distanceRadius, departureTime, timeRadius);
    List<PathNode> currentPaths = new LinkedList<PathNode>();
    for (Trip trip : startingTrips) {
      currentPaths.add(new PathNode(trip));
    }

    // Search for valid paths to destination
    while (!currentPaths.isEmpty()) {
      List<PathNode> nextPaths = new LinkedList<PathNode>();
      for (PathNode node : currentPaths) {
        if (node.trips.size() < 3) {
          for (Trip nextTrip : database.getConnectedTrips(
              node.current.getEndingLatitude(),
              node.current.getEndingLongitude(), distanceRadius,
              node.current.getEta(), CONNECTION_WAIT_TIME)) {
            node.addTrip(nextTrip);
            if (isWithinDestinationRadius(nextTrip, distanceRadius, endLat,
                endLon)) {
              paths.add(unwrap(node));
            } else {
              nextPaths.add(node);
            }
          }
        }
      }
      currentPaths = nextPaths;
    }
    Collections.sort(paths, TRIP_COMPARATOR);
    return paths;
  }
}
