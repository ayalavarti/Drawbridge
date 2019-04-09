package edu.brown.cs.drawbridge.main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import com.sun.org.apache.xpath.internal.operations.Mod;
import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import freemarker.template.Configuration;
import spark.*;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * An abstract class for the User Interface of the Java project. Contains
 * Handler objects for each end-point defined in the setEnpoints() method.
 *
 * @author mlavrent
 */
public class UserInterface {
  private static final Gson GSON = new Gson();
  private static DatabaseQuery dbQuery;

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
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
   *
   * @param dbName
   *                 The name of the database.
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
    Spark.post("/mapboxKey", new MapboxHandler());
    Spark.get("/results", new ListGetHandler(), freeMarker);

    Spark.get("/trip/:tid", new DetailGetHandler(), freeMarker);
    Spark.post("/trip/:tid", new DetailPostHandler(), freeMarker);

    Spark.get("/my-trips", new UserGetHandler(), freeMarker);

    Spark.get("/new", new CreateGetHandler(), freeMarker);
    Spark.post("/new", new CreatePostHandler(), freeMarker);

    Spark.get("/help", new InfoGetHandler(), freeMarker);

    Spark.notFound(new Code404Handler());
    Spark.internalServerError(new Code404Handler());
  }

  // ---------------------------- Home ------------------------------------
  /**
   * Handle requests to the home screen of the website.
   */
  private static class HomeGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Home")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "map.ftl");
    }
  }

  // ---------------------------- Home ------------------------------------
  /**
   * Handle requests to query the Mapbox API key.
   */
  private static class MapboxHandler implements Route {
    @Override
    public String handle(Request req, Response res) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("mapboxKey", System.getenv("MAPBOX_KEY")).build();
      return GSON.toJson(variables);
    }
  }

  // ---------------------------- List ------------------------------------

  /**
   * Class to handle getting results to display; This handles all requests
   * originating from the home page and from resubmitting the walking time
   * values.
   */
  private static class ListGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      // Get parameter values
      QueryParamsMap qm = request.queryMap();

      String uid = qm.value("userID");
      double walkTime, waitTime;
      if (qm.hasKey("walkTime")) {
        walkTime = qm.get("walkTime").doubleValue();
      } else {
        walkTime = 15 * 60; // 15 minutes walking is the default
      }

      if (qm.hasKey("waitTime")) {
        waitTime = qm.get("waitTime").doubleValue();
      } else {
        waitTime = 30 * 60; // 30 minutes is default for waiting for carpool
      }

      // TODO: replace with actual data getting.
      List<List<Trip>> suggestions = new ArrayList<>();
      List<Trip> s1 = new ArrayList<>();
      s1.add(dbQuery.DUMMY_TRIP);
      List<Trip> s2 = new ArrayList<>();
      s2.add(dbQuery.DUMMY_TRIP);
      s2.add(dbQuery.DUMMY_TRIP);
      suggestions.add(s1);
      suggestions.add(s2);

      // Process suggestions into required json form
      List<List<Map<String, String>>> tripData = new ArrayList<>();
      for (List<Trip> sugg : suggestions) {
        List<Map<String, String>> processedSugg = new ArrayList<>();

        for (Trip trip : sugg) {
          String status;
          if (trip.getMemberIds().contains(uid)) {
            status = "joined";
          } else if (trip.getHostId().equals(uid)) {
            status = "hosting";
          } else if (trip.getPendingIds().contains(uid)) {
            status = "pending";
          } else {
            status = "join";
          }

          Map<String, String> vars = new HashMap<String, String>();
          vars.put("start", trip.getStartingAddress());
          vars.put("end", trip.getEndingAddress());
          vars.put("date", Integer.toString(trip.getDepartureTime()));
          vars.put("currentSize", Integer.toString(trip.getCurrentSize()));
          vars.put("maxSize", Integer.toString(trip.getMaxUsers()));
          vars.put("costPerPerson", Double.toString(trip.getCostPerUser(uid)));
          vars.put("id", Integer.toString(trip.getId()));
          vars.put("name", trip.getName());
          vars.put("status", status);

          processedSugg.add(vars);
        }
        tripData.add(processedSugg);
      }


      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Results")
          .put("favicon", "images/favicon.png")
          .put("data", GSON.toJson(tripData)).build();
      return new ModelAndView(variables, "results.ftl");
    }
  }

  // --------------------------- Detail -----------------------------------
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

      // TODO: switch this out
      // Trip trip = dbQuery.getTripById(tid);
      Trip trip = dbQuery.DUMMY_TRIP;

      // Get user's names from the trip
      // User host = dbQuery.getUserById(trip.getHostId());
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

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", String.format("Drawbridge | %s", trip.getName()))
          .put("favicon", "images/favicon.png").put("trip", trip)
          .put("host", host).put("members", members).put("pending", pending)
          .build();
      return new ModelAndView(variables, "detail.ftl");
    }
  }

  /**
   * Handles various actions on the detail page including deleting a trip,
   * joining a trip, approving/denying pending members.
   */
  private static class DetailPostHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();

      int tid;
      try {
        tid = Integer.parseInt(request.params(":tid"));
      } catch (NumberFormatException e) {
        return null;
      }

      String action = qm.value("action");
      String uid = qm.value("user");
      System.out.println(request.body());

      if (action.equals("join")) {
        System.out.println("JOIN " + uid);

      } else if (action.equals("leave")) {
        System.out.println("LEAVE " + uid);

      } else if (action.equals("delete")) {
        System.out.println("DELETE");

      } else if (action.equals("approve")) {
        System.out.println("APPROVE " + uid);

      } else if (action.equals("deny")) {
        System.out.println("DENY " + uid);

      } else {

      }

      response.redirect("/trip/" + tid, 303);
      return null;
    }
  }

  // ---------------------------- User ------------------------------------
  /**
   * Handles the display of the "my trips" page. Simply returns the template.
   */
  private static class UserGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | My Trips")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "my-trips.ftl");
    }
  }

  /**
   * Handles getting the user's trips, split up by category.
   */
  private static class UserPostHandler implements Route {
    @Override
    public Object handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String uid = qm.value("userID");

      // Getting the data
      // TODO: replace with real data getting
      List<Trip> hosting = new ArrayList<>();
      hosting.add(dbQuery.DUMMY_TRIP);
      List<Trip> member = new ArrayList<>();
      member.add(dbQuery.DUMMY_TRIP);
      member.add(dbQuery.DUMMY_TRIP);
      List<Trip> pending = new ArrayList<>();

      // Processing into JSON format
      List<List<Map<String, String>>> data = new ArrayList<>();
      for (Trip trip : hosting) {
        List<Map<String, String>> innerList = new ArrayList<>();

        String status;
        if (trip.getMemberIds().contains(uid)) {
          status = "joined";
        } else if (trip.getHostId().equals(uid)) {
          status = "hosting";
        } else if (trip.getPendingIds().contains(uid)) {
          status = "pending";
        } else {
          status = "join";
        }

        Map<String, String> vals = new HashMap<>();
        vals.put("start", trip.getStartingAddress());
        vals.put("end", trip.getEndingAddress());
        vals.put("date", Integer.toString(trip.getDepartureTime()));
        vals.put("currentSize", Integer.toString(trip.getCurrentSize()));
        vals.put("maxSize", Integer.toString(trip.getMaxUsers()));
        vals.put("costPerPerson", Double.toString(trip.getCostPerUser(uid)));
        vals.put("id", Integer.toString(trip.getId()));
        vals.put("name", trip.getName());
        vals.put("status", status);

        data.add(innerList);
      }

      return GSON.toJson(data);
    }
  }

  // --------------------------- Create -----------------------------------
  /**
   * Handles loading the "create new trip" page. Simple template serving.
   */
  private static class CreateGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Create Trip")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "create.ftl");
    }
  }

  /**
   * Handles create form submission and actual creation of a new trip.
   */
  private static class CreatePostHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }

  // ---------------------------- Info ------------------------------------
  /**
   * Class to handle get requests to faq/help/info static page.
   */
  private static class InfoGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Info")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "info.ftl");
    }
  }

  // --------------------------- Errors -----------------------------------
  /**
   * Class to handle all page not found requests.
   */
  private static class Code404Handler implements Route {
    @Override
    public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Page Not Found")
          .put("favicon", "images/favicon.png").build();

      response.status(404);
      return new ModelAndView(variables, "not-found.ftl");
    }

  }
}
