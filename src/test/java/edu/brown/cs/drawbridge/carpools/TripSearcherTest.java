package edu.brown.cs.drawbridge.carpools;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.models.Trip;

public class TripSearcherTest {

  private static DatabaseQuery test;
  private static TripSearcher tripSearcher;

  /*
   * Test search method.
   */

  private void assertSamePaths(List<List<Trip>> paths1,
      List<List<String>> paths2) {
    for (int i = 0; i < paths1.size(); i++) {
      for (int j = 0; j < paths1.get(i).size(); j++) {
        assertEquals(paths1.get(i).get(j).getName(), paths2.get(i).get(j));
      }
      assertEquals(paths1.get(i).size(), paths2.get(i).size());
    }
    assertEquals(paths1.size(), paths2.size());
  }

  @BeforeClass
  public static void oneTimeSetUp() throws SQLException, MissingDataException {
    try {
      String username = System.getenv("DB_USER");
      String password = System.getenv("DB_PASS");
      /*
       * Run the following queries in pgadmin: CREATE USER <username> WITH
       * PASSWORD '<password>' GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA
       * public TO <username> GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA
       * public TO <username>
       */
      test = new DatabaseQuery("//127.0.0.1:5432/searchTester", username,
          password);
      tripSearcher = new TripSearcher(test);
    } catch (ClassNotFoundException | SQLException e) {
      assert false;
    }
  }

  private void print(List<List<Trip>> paths) {
    for (List<Trip> path : paths) {
      System.out.println("Path:");
      for (Trip trip : path) {
        System.out.println(trip.getName());
      }
    }
  }

  @Test
  public void testSet1() {
    List<List<String>> expected = new ArrayList<List<String>>();
    // Test 1
    expected.add(new ArrayList<String>(Arrays.asList("1H", "1I")));
    assertSamePaths(
        tripSearcher.searchWithoutId(40, 40, 40, 40, 300, 0.85, 500), expected);
    // Test 2
    assertSamePaths(
        tripSearcher.searchWithoutId(40, 40, 39.995, 39.998, 300, 0.85, 1500),
        expected);
    // Test 3
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("1A")));
    assertSamePaths(
        tripSearcher.searchWithoutId(40, 40, 40.5, 40.5, 300, 0.85, 1500),
        expected);
    // Test 4
    expected = new ArrayList<List<String>>();
    assertSamePaths(tripSearcher.searchWithoutId(40, 40, 41, 41, 300, 0, 1500),
        expected);
    // Test 5
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1D")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1C")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1E")));
    assertSamePaths(tripSearcher.searchWithoutId(40, 40, 41, 41, 300, 0.85, 0),
        expected);
    // Test 6
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("1H", "1J")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1D", "1G")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1C", "1G")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1E", "1G")));
    assertSamePaths(
        tripSearcher.searchWithoutId(40, 40, 42, 41, 300, 0.85, 1500),
        expected);
    // Test 7
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1D")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1C")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1E")));
    assertSamePaths(
        tripSearcher.searchWithoutId(40, 40, 41, 41, 300, 0.85, 1500),
        expected);
    // Test 8
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1D", "1G")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1C", "1G")));
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1E", "1G")));
    assertSamePaths(
        tripSearcher.searchWithoutId(40.006, 40.007, 42, 41, 300, 0.85, 1500),
        expected);
    // Test 9
    expected = new ArrayList<List<String>>();
    assertSamePaths(
        tripSearcher.searchWithoutId(40, 40, 12, 14, 300, 0.85, 1500),
        expected);
    // Test 10
    assertSamePaths(
        tripSearcher.searchWithoutId(-8, -8, 41, 41, 300, 0.85, 1500),
        expected);
    // Test 11
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("1K", "1L")));
    assertSamePaths(
        tripSearcher.searchWithoutId(41, 41.006, 42, 41, 7500, 0.25, 1500),
        expected);
    // Test 12
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("1A", "1D")));
    assertSamePaths(
        tripSearcher.searchWithoutId(40.005, 40.005, 41, 41, 300, 0, 1500),
        expected);
  }

  @Test
  public void testSet2() {
    List<List<String>> expected = new ArrayList<List<String>>();
    // Test 1
    expected.add(new ArrayList<String>(Arrays.asList("2B", "2F")));
    expected.add(new ArrayList<String>(Arrays.asList("2A", "2F")));
    assertSamePaths(tripSearcher.searchWithoutId(0, 0, 0.65, 0.67, 50, 1.3, 0),
        expected);
    // Test 2
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("2D")));
    expected.add(new ArrayList<String>(Arrays.asList("2B", "2F")));
    expected.add(new ArrayList<String>(Arrays.asList("2A", "2F")));
    /*
     * Should pass without the next line.
     */
    expected.add(new ArrayList<String>(Arrays.asList("2C", "2F")));
    assertSamePaths(tripSearcher.searchWithoutId(0, 0, 0.65, 0.67, 50, 1.6, 0),
        expected);
    // Test 3
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("2B", "2G")));
    expected.add(new ArrayList<String>(Arrays.asList("2A", "2G")));
    expected.add(new ArrayList<String>(Arrays.asList("2C", "2G")));
    assertSamePaths(tripSearcher.searchWithoutId(0, 0, 0.502, 0.5, 50, 1.3, 0),
        expected);
    // Test 4
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("2A", "2E")));
    assertSamePaths(tripSearcher.searchWithoutId(0, 0, 0.71, 0.75, 50, 1.3, 0),
        expected);
    // Test 5
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("2B")));
    expected.add(new ArrayList<String>(Arrays.asList("2C")));
    assertSamePaths(tripSearcher.searchWithoutId(0, 0, 0.36, 0.345, 50, 1.3, 0),
        expected);
  }

  @Test
  public void testSet3() {
    List<List<String>> expected = new ArrayList<List<String>>();
    // Test 1
    expected.add(new ArrayList<String>(Arrays.asList("3A")));
    expected.add(new ArrayList<String>(Arrays.asList("3B")));
    expected.add(new ArrayList<String>(Arrays.asList("3C")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.5, -16.5, 20, 0, 10),
        expected);
    // Test 2
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3A")));
    expected.add(new ArrayList<String>(Arrays.asList("3B")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.5, -16.5, 15, 0, 5),
        expected);
    // Test 3
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3C")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.5, -16.5, 35, 0, 5),
        expected);
    // Test 4
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3L")));
    expected.add(new ArrayList<String>(Arrays.asList("3K")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3E")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3E")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3D")));
    expected.add(new ArrayList<String>(Arrays.asList("3A", "3E")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3F")));
    expected.add(new ArrayList<String>(Arrays.asList("3A", "3F")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3F")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.7, -16.8, 20, 0, 10),
        expected);
    // Test 5
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3L")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3F")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3E")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.7, -16.8, 30, 0, 5),
        expected);
    // Test 6
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3K", "3I")));
    expected.add(new ArrayList<String>(Arrays.asList("3L", "3J")));
    expected.add(new ArrayList<String>(Arrays.asList("3K", "3H")));
    expected.add(new ArrayList<String>(Arrays.asList("3L", "3G")));
    expected.add(new ArrayList<String>(Arrays.asList("3L", "3H")));
    expected.add(new ArrayList<String>(Arrays.asList("3L", "3I")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3F", "3H")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3E", "3I")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3D", "3G")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3F", "3I")));
    assertSamePaths(tripSearcher.searchWithoutId(-16, -16, -17, -17, 20, 0, 5),
        expected);
    // Test 7
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3E", "3I")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3F", "3I")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3F", "3H")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3E", "3H")));
    assertSamePaths(tripSearcher.searchWithoutId(-16, -16, -17, -17, 60, 0, 30),
        expected);
  }

  @Test
  public void testSet4() {
    List<List<String>> expected = new ArrayList<List<String>>();
    // Test 1
    expected.add(new ArrayList<String>(Arrays.asList("4A")));
    assertSamePaths(tripSearcher.searchWithoutId(75, 100, 80, 100, 0, 0, 0),
        expected);
    // Test 2
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("4B")));
    expected.add(new ArrayList<String>(Arrays.asList("4C", "4D")));
    assertSamePaths(
        tripSearcher.searchWithoutId(80, 100, 82, 106, 80000, 115, 0),
        expected);
    // Test 3
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("4A", "4B")));
    expected.add(new ArrayList<String>(Arrays.asList("4A", "4C", "4D")));
    assertSamePaths(tripSearcher.searchWithoutId(75, 100, 82, 106, 1, 115, 2),
        expected);
    // Test 4
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("4A", "4B", "4E")));
    assertSamePaths(
        tripSearcher.searchWithoutId(75, 100, 83, 110, 100, 115, 101),
        expected);
  }
}
