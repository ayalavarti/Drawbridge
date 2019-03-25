package edu.brown.cs.drawbridge.main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import freemarker.template.Configuration;
import javafx.util.Pair;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * An abstract class for the User Interface of the Java project. Contains
 * Handler objects for each end-point defined in the setEnpoints() method.
 *
 * @author mlavrent
 */
public class UserInterface {
  private static final Gson GSON = new Gson();
  private static final String MAPBOX_TOKEN = "pk.eyJ1IjoiYXJ2Mzk1IiwiYSI6ImNqdGpodWcwdDB6dXEzeXBrOHJyeGVpNm8ifQ.bAwH-KG_5A5kwIxCf6xCSQ";
  private static DatabaseQuery dbQuery;

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates =
            new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Method to set the database to use when querying.
   * @param dbName The name of the database.
   * @return true when the set is successful; false when unsuccessful.
   */
  public static boolean setDB(String dbName) {
    try {
      String dbUser = System.getenv("DB_USER");
      String dbPass = System.getenv("DB_PASS");
      dbQuery = new DatabaseQuery(dbName, dbUser, dbPass);
      return true;
    } catch (SQLException | ClassNotFoundException e) {
      return false;
    }
  }

  /**
   * Instantiates the resources for the analyzers and sets the end-points for
   * the front end GUI.
   */
  public static void setEndpoints() {
    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/", new HomeGetHandler(), freeMarker);

    Spark.get("/results", new ListGetHandler(), freeMarker);
    Spark.post("/results", new ListPostHandler(), freeMarker);

    Spark.get("/trip/:tid", new DetailGetHandler(), freeMarker);

    Spark.get("/my-trips/:uid", new UserGetHandler(), freeMarker);

    Spark.get("/new", new CreateGetHandler(), freeMarker);
    Spark.post("/new", new CreatePostHandler(), freeMarker);

    Spark.get("/help", new InfoGetHandler(), freeMarker);
  }

  // ---------------------------- Home ------------------------------------
  /**
   * Handle requests to the home screen of the website.
   */
  private static class HomeGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      // Return empty data to GUI when / route is called
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Home")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "map.ftl");
    }
  }

  //---------------------------- List ------------------------------------
  private static class ListGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      // Return empty data to GUI when / route is called
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Info")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "info.ftl");
    }
  }
  private static class ListPostHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }

  //--------------------------- Detail -----------------------------------
  /**
   * Handler to get information about a specific trip and display it on a page.
   */
  private static class DetailGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      int tid;
      try {
        tid = Integer.parseInt(request.params(":tid"));
      } catch (NumberFormatException e) {
        return null;
      }

      Trip trip = dbQuery.getTripById(tid);

      // Get human-readable names for the addresses
      String startName, endName;

      MapboxGeocoding startGeocode = MapboxGeocoding.builder()
              .accessToken(MAPBOX_TOKEN)
              .query(Point.fromLngLat(trip.getStartingLongitude(),
                                      trip.getStartingLatitude()))
              .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
              .build();
      try {
        startName =
                startGeocode.executeCall().body().features().get(0).placeName();
      } catch (Exception e) {
        String lngDir = trip.getStartingLongitude() < 0 ? "°S" : "°N";
        String latDir = trip.getStartingLatitude() < 0 ? "°W" : "°E";
        startName = Math.abs(trip.getStartingLatitude()) + latDir + ", "
                  + Math.abs(trip.getStartingLongitude()) + lngDir;
      }

      MapboxGeocoding endGeocode = MapboxGeocoding.builder()
              .accessToken(MAPBOX_TOKEN)
              .query(Point.fromLngLat(trip.getEndingLongitude(),
                                      trip.getEndingLatitude()))
              .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
              .build();
      try {
        endName =
                endGeocode.executeCall().body().features().get(0).placeName();
      } catch (Exception e) {
        String lngDir = trip.getEndingLongitude() < 0 ? "°S" : "°N";
        String latDir = trip.getEndingLatitude() < 0 ? "°W" : "°E";
        endName = Math.abs(trip.getEndingLatitude()) + latDir + ", "
                + Math.abs(trip.getEndingLongitude()) + lngDir;
      }

      // Get user's names from the trip
//      User host = dbQuery.getUserById(trip.getHostId());
      User host = new User(trip.getHostId(), "Mary III", "mary@queen.com");
      List<User> members = new ArrayList<>();
      for (String memId : trip.getMemberIds()) {
        members.add(dbQuery.getUserById(memId));
      }
      List<User> pending = new ArrayList<>();
      for (String pendId : trip.getPendingIds()) {
        pending.add(dbQuery.getUserById(pendId));
      }
      // TODO: remove this; for testing purposes only
      pending.add(new User("1", "Mark Lavrentyev", "lavrema@outlook.com"));
      members.add(new User("2", "Arvind Yalavarti", "abc@example.com"));

      // Return empty data to GUI when / route is called
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", trip.getName())
          .put("favicon", "images/favicon.png")
          .put("trip", trip)
          .put("startName", startName)
          .put("endName", endName)
          .put("host", host)
          .put("members", members)
          .put("pending", pending).build();
      return new ModelAndView(variables, "detail.ftl");
    }
  }

  //---------------------------- User ------------------------------------
  private static class UserGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }

  //--------------------------- Create -----------------------------------
  private static class CreateGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }
  private static class CreatePostHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }

  //---------------------------- Info ------------------------------------
  /**
   * Class to handle get requests to faq/help/info static page.
   */
  private static class InfoGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      // Return empty data to GUI when / route is called
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Info")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "info.ftl");
    }
  }

  //--------------------------- Errors -----------------------------------
//  private static class Code404Handler implements TemplateViewRoute {
//
//  }
}
