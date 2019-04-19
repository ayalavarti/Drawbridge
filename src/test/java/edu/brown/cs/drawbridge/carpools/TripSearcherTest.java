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

  //@Test
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
    expected.add(new ArrayList<String>(Arrays.asList("3K")));
    expected.add(new ArrayList<String>(Arrays.asList("3L")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3E")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3F")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3E")));
    expected.add(new ArrayList<String>(Arrays.asList("3B", "3D")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.7, -16.8, 20, 0, 10),
        expected);
    // Test 5
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3L")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3E")));
    expected.add(new ArrayList<String>(Arrays.asList("3C", "3F")));
    assertSamePaths(
        tripSearcher.searchWithoutId(-16, -16, -16.7, -16.8, 30, 0, 5),
        expected);
    // Test 6
    expected = new ArrayList<List<String>>();
    expected.add(new ArrayList<String>(Arrays.asList("3K", "3H")));
    expected.add(new ArrayList<String>(Arrays.asList("3L", "3G")));
    expected.add(new ArrayList<String>(Arrays.asList("3K", "3I")));
    expected.add(new ArrayList<String>(Arrays.asList("3L", "3J")));
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
}
