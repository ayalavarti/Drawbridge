package edu.brown.cs.drawbridge.main;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import edu.brown.cs.drawbridge.carpools.Carpools;
import edu.brown.cs.drawbridge.database.DatabaseQuery;
import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract class for the User Interface of the Java project. Contains
 * Handler objects for each end-point defined in the setEnpoints() method.
 *
 * @author Mark Lavrentyev
 */
public final class UserInterface {

  private static final Gson GSON = new Gson();
  private static final String MAPBOX_TOKEN = System.getenv("MAPBOX_KEY");
  private static Carpools carpools;

  private UserInterface() {
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out
          .printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * Method to set the database to use when querying.
   *
   * @param dbName
   *     The name of the database.
   *
   * @return true when the set is successful; false when unsuccessful.
   */
  public static boolean setDB(String dbName) {
    try {
      carpools = new Carpools(dbName, System.getenv("DB_USER"),
          System.getenv("DB_PASS"));
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

    Spark.get("/trip/:tid", new DetailGetHandler(), freeMarker);
    Spark.post("/trip/:tid", new DetailPostHandler());

    Spark.get("/my-trips", new UserGetHandler(), freeMarker);
    Spark.post("/my-trips", new UserPostHandler());

    Spark.get("/new", new CreateGetHandler(), freeMarker);
    Spark.post("/new", new CreatePostHandler());

    Spark.get("/help", new InfoGetHandler(), freeMarker);

    Spark.get("/*", new Code404Handler(), freeMarker);

    Spark.internalServerError((req, res) -> {
      res.redirect("/error");
      return null;
    });
  }

  // ---------------------------- Home ------------------------------------

  @SafeVarargs
  private static List<List<Map<String, String>>> processToJSON(String uid,
      List<Trip>... preProcessed) {
    List<List<Map<String, String>>> data = new ArrayList<>();
    for (List<Trip> entry : preProcessed) {

      List<Map<String, String>> innerList = new ArrayList<>();
      for (Trip trip : entry) {
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
        if (uid != null) {
          vals.put("costPerPerson", Double.toString(trip.getCostPerUser(uid)));
        } else {
          vals.put("costPerPerson", Double.toString(trip.getCostPerUser("")));
        }
        vals.put("id", Integer.toString(trip.getId()));
        vals.put("name", trip.getName());
        vals.put("status", status);

        innerList.add(vals);
      }
      data.add(innerList);
    }
    return data;
  }

  // ---------------------------- List ------------------------------------

  /**
   * Handle requests to the home screen of the website.
   */
  private static class HomeGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Home").put("mapboxKey", MAPBOX_TOKEN)
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "map.ftl");
    }
  }

  // --------------------------- Detail -----------------------------------

  /**
   * Class to handle getting results to display; This handles all requests
   * originating from the home page and from resubmitting the walking time
   * values.
   */
  private static class ListGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response) {
      // Get parameter values
      QueryParamsMap qm = request.queryMap();
      List<List<Map<String, String>>> data;

      try {
        String startName = qm.value("startName");
        String endName = qm.value("endName");
        double startLat = Double.parseDouble(qm.value("startLat"));
        double startLon = Double.parseDouble(qm.value("startLon"));
        long datetime = Long.parseLong(qm.value("date"));
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
        List<Trip> s1 = new ArrayList<>();
        s1.add(DatabaseQuery.DUMMY_TRIP);
        List<Trip> s2 = new ArrayList<>();
        s2.add(DatabaseQuery.DUMMY_TRIP);
        s2.add(DatabaseQuery.DUMMY_TRIP);

        data = processToJSON(uid, s1, s2);
      } catch (NullPointerException e) {
        data = new ArrayList<>();
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Results")
          .put("favicon", "images/favicon.png").put("data", GSON.toJson(data))
          .build();

      return new ModelAndView(variables, "results.ftl");
    }
  }

  /**
   * Handler to get information about a specific trip and display it on a page.
   */
  private static class DetailGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response)
        throws SQLException {
      int tid;
      Trip trip;
      List<List<User>> people;
      try {
        tid = Integer.parseInt(request.params(":tid"));
        trip = carpools.getTrip(tid);
        people = carpools.getUsers(tid);
      } catch (NumberFormatException | MissingDataException e) {
        response.status(404);
        Map<String, Object> variables
            = new ImmutableMap.Builder<String, Object>()
            .put("title", String.format("Drawbridge | Not Found"))
            .put("favicon", "images/favicon.png").build();
        return new ModelAndView(variables, "not-found.ftl");
      }

      User host = people.get(0).get(0);
      List<User> members = people.get(1);
      List<User> pending = people.get(2);

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

  // ---------------------------- User ------------------------------------

  /**
   * Handles various actions on the detail page including deleting a trip,
   * joining a trip, approving/denying pending members.
   */
  private static class DetailPostHandler implements Route {
    @Override public ModelAndView handle(Request request, Response response) {
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

  /**
   * Handles the display of the "my trips" page. Simply returns the template.
   */
  private static class UserGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response) {
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
    @Override public Object handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String uid = qm.value("userID");

      // Getting the data
      // TODO: replace with real data getting
      List<Trip> hosting = new ArrayList<>();
      hosting.add(DatabaseQuery.DUMMY_TRIP2);
      List<Trip> member = new ArrayList<>();
      member.add(DatabaseQuery.DUMMY_TRIP);
      member.add(DatabaseQuery.DUMMY_TRIP);
      List<Trip> pending = new ArrayList<>();
      pending.add(DatabaseQuery.DUMMY_TRIP);

      return GSON.toJson(processToJSON(uid, hosting, member, pending));
    }
  }

  // --------------------------- Create -----------------------------------

  /**
   * Handles loading the "create new trip" page. Simple template serving.
   */
  private static class CreateGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Create Trip")
          .put("mapboxKey", MAPBOX_TOKEN).put("favicon", "images/favicon.png")
          .build();

      return new ModelAndView(variables, "create.ftl");
    }
  }

  /**
   * Handles create form submission and actual creation of a new trip.
   */
  private static class CreatePostHandler implements Route {
    @Override public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }

  // ---------------------------- Info ------------------------------------

  /**
   * Class to handle get requests to faq/help/info static page.
   */
  private static class InfoGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response) {
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
  private static class Code404Handler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Page Not Found")
          .put("favicon", "images/favicon.png").build();

      response.status(404);
      return new ModelAndView(variables, "not-found.ftl");
    }

  }
}
