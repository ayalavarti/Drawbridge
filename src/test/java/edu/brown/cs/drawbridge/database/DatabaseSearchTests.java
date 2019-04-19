package edu.brown.cs.drawbridge.database;

import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Disclaimer:
 * This is a working test class for the tripSearcher
 * functions on the database. It expects that there are 2 separate databases
 * built from the carpools schema for containing data which is to remain
 * unchanged: dummyDatabase and searchTester. You may change your database
 * names, but ensure that you grant privileges and adjust the urls accordingly.
 * <p>
 * Test sets contain 'if(false)' blocks for inserting the data set. If you
 * have restored the data from the testSearchData dump file, leave these alone.
 * If you have not done this, change all conditions to true, run the class ONCE,
 * then change them back to false.
 */
public class DatabaseSearchTests {

  private static DatabaseQuery searchTester;

  @BeforeClass public static void oneTimeSetUp()
      throws SQLException, ClassNotFoundException {
    String username = "dev";
    String password = "dev";
    /*
     * Run the following queries in pgadmin:
     * CREATE USER <username> WITH PASSWORD '<password>'
     * GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO <username>
     * GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO <username>
     */
    searchTester = new DatabaseQuery("//127.0.0.1:5432/searchTester", username,
        password);
    final User dummyHost = new User("1", "one", "one@mail.com");
    //searchTester.addUser(dummyHost);
  }

  public static boolean hasAllTrips(List<String> nameList, List<Trip> trips) {
    boolean same = true;
    for (Trip t : trips) {
      if (!nameList.contains(t.getName())) {
        same = false;
      }
    }
    return same && (nameList.size() == trips.size());
  }

  @Test public void testSet1() throws SQLException, MissingDataException {
    if (false) {
      List<String> nameList = new ArrayList<>(Arrays
          .asList("1A", "1B", "1C", "1D", "1E", "1F", "1G", "1H", "1I", "1J",
              "1K", "1L"));
      double[] lat1List = { 40.005, 40.01, 40.5, 40.5, 40.494, 40.491, 41.003,
          39.996, 42.999, 43.003, 41, 41.5
      };
      double[] lon1List = { 40.005, 39.989, 40.507, 40.5, 40.501, 40.491,
          41.004, 39.998, 40.001, 40.002, 41.007, 41.002
      };
      double[] lat2List = { 40.5, 40.5, 41, 41, 41, 41, 42, 43, 40, 42, 41.5,
          42
      };
      double[] lon2List = { 40.5, 40.5, 41, 41, 41, 41, 41, 40, 40, 41, 41, 41
      };
      long[] departureList = { 300, 300, 5000, 5000, 5000, 5000, 7500, 300,
          8800, 8800, 7500, 10000
      };
      long[] etaList = { 4000, 4000, 7000, 7000, 7000, 7000, 9500, 8000, 20000,
          12000, 9500, 12000
      };
      for (int i = 0; i < nameList.size(); i++) {
        searchTester.createTrip(Trip.TripBuilder.newTripBuilder()
            .addIdentification(-1, nameList.get(i))
            .addLocations(lat1List[i], lon1List[i], lat2List[i], lon2List[i])
            .addAddressNames("", "").addTimes(departureList[i], etaList[i])
            .addDetails(2, 2.00, "", "", "").build(), "1");
      }
    }
    //these conditions should give paths that traverse the entire set
    //specific paths to follow soon
    List<Trip> s1 = searchTester
        .getConnectedTripsWithinTimeRadius(40, 40, 0.85, 300, 1500);
    assertTrue(hasAllTrips(new ArrayList<>(Arrays.asList("1A", "1H")), s1));

    List<Trip> s2 = searchTester
        .getConnectedTripsWithinTimeRadius(40, 40, 1.6, 300, 1500);
    assertTrue(
        hasAllTrips(new ArrayList<>(Arrays.asList("1A", "1B", "1H")), s2));
  }

  @Test public void testSet2() throws SQLException, MissingDataException {
    if (false) {
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2A")
              .addLocations(0.008, 0.008, 0.36, 0.36).addAddressNames("", "")
              .addTimes(50, 300).addDetails(2, 2.00, "", "", "").build(), "1");
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2B")
              .addLocations(-0.001, -0.004, 0.352, 0.347)
              .addAddressNames("", "").addTimes(50, 300)
              .addDetails(2, 2.00, "", "", "").build(), "1");
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2C")
              .addLocations(-0.005, 0, 0.369, 0.35).addAddressNames("", "")
              .addTimes(50, 300).addDetails(2, 2.00, "", "", "").build(), "1");
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2D")
              .addLocations(0.01, 0.01, 0.648, 0.668).addAddressNames("", "")
              .addTimes(50, 1000).addDetails(2, 2.00, "", "", "").build(), "1");
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2E")
              .addLocations(0.361, 0.361, 0.71, 0.75).addAddressNames("", "")
              .addTimes(1500, 3000).addDetails(2, 2.00, "", "", "").build(),
          "1");
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2F")
              .addLocations(0.356, 0.353, 0.656, 0.674).addAddressNames("", "")
              .addTimes(1500, 3000).addDetails(2, 2.00, "", "", "").build(),
          "1");
      searchTester.createTrip(
          Trip.TripBuilder.newTripBuilder().addIdentification(-1, "2G")
              .addLocations(0.359, 0.351, 0.502, 0.5).addAddressNames("", "")
              .addTimes(1500, 3000).addDetails(2, 2.00, "", "", "").build(),
          "1");
    }
    //these conditions should give paths that traverse the entire set
    //specific paths to follow soon
    List<Trip> s1 = searchTester
        .getConnectedTripsWithinTimeRadius(0, 0, 1.3, 50, 2000);
    assertTrue(
        hasAllTrips(new ArrayList<>(Arrays.asList("2A", "2B", "2C")), s1));
  }

  @Test public void testSet3() throws SQLException, MissingDataException {
    if (false) {
      List<String> nameList = new ArrayList<>(Arrays
          .asList("3A", "3B", "3C", "3D", "3E", "3F", "3G", "3H", "3I", "3J",
              "3K", "3L"));
      double[] lat1List = { -16, -16, -16, -16.5, -16.5, -16.5, -16.7, -16.7,
          -16.7, -16.7, -16, -16
      };
      double[] lon1List = { -16, -16, -16, -16.5, -16.5, -16.5, -16.8, -16.8,
          -16.8, -16.8, -16, -16
      };
      double[] lat2List = { -16.5, -16.5, -16.5, -16.7, -16.7, -16.7, -17, -17,
          -17, -17, -16.7, -16.7
      };
      double[] lon2List = { -16.5, -16.5, -16.5, -16.8, -16.8, -16.8, -17, -17,
          -17, -17, -16.8, -16.8
      };
      long[] departureList = { 10, 20, 30, 125, 140, 150, 205, 235, 240, 225,
          15, 25
      };
      long[] etaList = { 130, 120, 135, 200, 230, 225, 500, 505, 590, 800, 230,
          200
      };
      for (int i = 0; i < nameList.size(); i++) {
        searchTester.createTrip(Trip.TripBuilder.newTripBuilder()
            .addIdentification(-1, nameList.get(i))
            .addLocations(lat1List[i], lon1List[i], lat2List[i], lon2List[i])
            .addAddressNames("", "").addTimes(departureList[i], etaList[i])
            .addDetails(2, 2.00, "", "", "").build(), "1");
      }
    }
  }

}
