package edu.brown.cs.drawbridge.main;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.brown.cs.drawbridge.carpools.Carpools;
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
import java.util.Arrays;
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

  /**
   * Default constructor made private since UserInterface is a utility class.
   */
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

    Spark.get("/my-trips", new MyTripsGetHandler(), freeMarker);
    Spark.post("/my-trips", new MyTripsPostHandler());

    Spark.get("/new", new CreateGetHandler(), freeMarker);
    Spark.post("/new", new CreatePostHandler());

    Spark.post("/login", new UserLoginHandler());

    Spark.get("/help", new InfoGetHandler(), freeMarker);
    Spark.get("/error", new ServerErrorHandler(), freeMarker);

    Spark.get("/*", new Code404Handler(), freeMarker);

    Spark.internalServerError((req, res) -> {
      res.redirect("/error");
      return null;
    });
  }

  // --------------------------- Helpers -----------------------------------

  /**
   * Overloaded method to provide an alternate signature for the
   * JSON-processing method.
   *
   * @param uid
   *     The user id for which to create this JSON object.
   * @param tripGroups
   *     a trip-group array.
   *
   * @return A JSON-encodable data structure for trip-groups.
   */
  @SafeVarargs private static List<List<Map<String, String>>> processToJSON(
      String uid, List<Trip>... tripGroups) {

    return processToJSON(uid, Arrays.asList(tripGroups));
  }

  /**
   * Method to help process a list of trip-groups into a JSON-encodable format.
   *
   * @param uid
   *     The user id this is being compiled for.
   * @param tripGroupList
   *     The list of troup-grips.
   *
   * @return A JSON-encodable list of groups of trip objects.
   */
  private static List<List<Map<String, String>>> processToJSON(String uid,
      List<List<Trip>> tripGroupList) {
    List<List<Map<String, String>>> data = new ArrayList<>();
    for (List<Trip> entry : tripGroupList) {

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
        vals.put("date", Long.toString(trip.getDepartureTime()));
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

  // ---------------------------- Home ------------------------------------

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

  // ---------------------------- List ------------------------------------

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
      JsonObject payload = new JsonObject();

      try {
        String startName = qm.value("startName");
        String endName = qm.value("endName");

        double startLat = Double.parseDouble(qm.value("startLat"));
        double startLon = Double.parseDouble(qm.value("startLon"));
        double endLat = Double.parseDouble(qm.value("endLat"));
        double endLon = Double.parseDouble(qm.value("endLon"));

        long datetime = Long.parseLong(qm.value("date"));
        String uid = qm.value("userID");

        double walkTime, waitTime;
        if (qm.hasKey("walkTime")) {
          walkTime = qm.get("walkTime").doubleValue();
        } else {
          walkTime = 15; // 15 minutes walking is the default
        }
        if (qm.hasKey("waitTime")) {
          waitTime = qm.get("waitTime").doubleValue();
        } else {
          waitTime = 30; // 30 minutes is default for waiting for carpool
        }

        // Mirror the inputted values back
        payload.addProperty("startName", startName);
        payload.addProperty("endName", endName);
        payload.addProperty("startLat", startLat);
        payload.addProperty("startLon", startLon);
        payload.addProperty("endLat", endLat);
        payload.addProperty("endLon", endLon);
        payload.addProperty("date", datetime);
        payload.addProperty("walkTime", walkTime);
        payload.addProperty("waitTime", waitTime);

        List<List<Trip>> results;
        // Perform search
        if (uid == null) {
          results = carpools
              .searchWithoutId(startLat, startLon, endLat, endLon, datetime,
                  (long) walkTime, (long) waitTime);
        } else {
          results = carpools
              .searchWithId(uid, startLat, startLon, endLat, endLon, datetime,
                  (long) walkTime, (long) waitTime);
        }
        data = processToJSON(uid, results);

      } catch (RuntimeException e) {
        data = new ArrayList<>();
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Results")
          .put("favicon", "images/favicon.png").put("data", GSON.toJson(data))
          .put("query", GSON.toJson(payload)).build();

      return new ModelAndView(variables, "results.ftl");
    }
  }

  // --------------------------- Detail -----------------------------------

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

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", String.format("Drawbridge | %s", trip.getName()))
          .put("favicon", "images/favicon.png").put("mapboxKey", MAPBOX_TOKEN)
          .put("trip", trip).put("host", host).put("members", members)
          .put("pending", pending).build();
      return new ModelAndView(variables, "detail.ftl");
    }
  }

  /**
   * Handles various actions on the detail page including deleting a trip,
   * joining a trip, approving/denying pending members.
   */
  private static class DetailPostHandler implements Route {
    @Override public Object handle(Request request, Response response) {
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

      /* Response will have the following:
       *   success - whether the action was successful
       *   payload - inputted data with {action, userID, tripID}
       *   redirect - if need to redirect, redirect to this url
       *   error - if success false, error message
       */
      JsonObject responseData = new JsonObject();

      JsonObject payload = new JsonObject();
      payload.addProperty("action", action);
      payload.addProperty("userID", uid);
      payload.addProperty("tripID", tid);
      responseData.add("payload", payload);

      boolean success = false;
      try {
        if (action.equals("join")) {
          success = carpools.joinTrip(tid, uid);

        } else if (action.equals("leave")) {
          success = carpools.leaveTrip(tid, uid);
          success &= carpools.rejectRequest(tid, uid, uid);

        } else if (action.equals("delete")) {
          success = carpools.deleteTrip(tid, uid);
          responseData.addProperty("redirect", "/my-trips");

        } else if (action.equals("approve")) {
          String pendingUID = qm.value("pendingUser");
          success = carpools.approveRequest(tid, uid, pendingUID);

        } else if (action.equals("deny")) {
          String pendingUID = qm.value("pendingUser");
          success = carpools.rejectRequest(tid, uid, pendingUID);
        }
        assert success; // Make sure the db action was completed successfully
        responseData.addProperty("success", true);

      } catch (SQLException | MissingDataException | AssertionError e) {
        responseData.addProperty("success", false);
        responseData.addProperty("error", e.getMessage());
      }

      return GSON.toJson(responseData);
    }
  }

  // -------------------------- My Trips ----------------------------------

  /**
   * Handles the display of the "my trips" page. Simply returns the template.
   */
  private static class MyTripsGetHandler implements TemplateViewRoute {
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
  private static class MyTripsPostHandler implements Route {
    @Override public Object handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();
      String uid = qm.value("userID");

      // Getting the data
      try {
        List<List<Trip>> userTrips = carpools.getTrips(uid);

        List<Trip> hosting = userTrips.get(0);
        List<Trip> member = userTrips.get(1);
        List<Trip> pending = userTrips.get(2);

        return GSON.toJson(processToJSON(uid, hosting, member, pending));
      } catch (SQLException | MissingDataException e) {
        // TODO: decide stuff to do
        return GSON.toJson(processToJSON(uid));
      }
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
    @Override public Object handle(Request request, Response response)
        throws SQLException, MissingDataException {
      QueryParamsMap qm = request.queryMap();

      // Read inputted values from request
      String tripName = qm.value("name");
      String startName = qm.value("startName");
      String endName = qm.value("endName");

      double startLat = Double.parseDouble(qm.value("startLat"));
      double startLon = Double.parseDouble(qm.value("startLon"));
      double endLat = Double.parseDouble(qm.value("endLat"));
      double endLon = Double.parseDouble(qm.value("endLon"));

      long departureTime = Long.parseLong(qm.value("date"));
      long eta = (long) (Double.parseDouble(qm.value("eta")) * 60);
      int maxSize = Integer.parseInt(qm.value("size"));
      double totalPrice = Double.parseDouble(qm.value("price"));

      String phone = qm.value("phone");
      String comments = qm.value("comments");
      String method = qm.value("method");

      String hostID = qm.value("userID");

      // Create a new trip through the carpool class
      Trip newTrip = Trip.TripBuilder.newTripBuilder()
          .addIdentification(-1, tripName)
          .addLocations(startLat, startLon, endLat, endLon)
          .addAddressNames(startName, endName).addTimes(departureTime, eta)
          .addDetails(maxSize, totalPrice, phone, method, comments)
          .buildWithUsers(hostID, new ArrayList<>(), new ArrayList<>());

      int tid = carpools.createTrip(newTrip, hostID);

      JsonObject responseData = new JsonObject();
      responseData.addProperty("success", true);
      responseData.addProperty("redirect", "/trip/" + tid);
      return GSON.toJson(responseData);
    }
  }

  // -------------------------- My Trips ----------------------------------

  /**
   * Handler for checking if a logged-in user is already in the database or
   * if they need to be added.
   */
  private static class UserLoginHandler implements Route {
    @Override public Object handle(Request request, Response response) {
      QueryParamsMap qm = request.queryMap();

      String uid = qm.value("userID");
      String name = qm.value("name");
      String email = qm.value("email");

      User user = new User(uid, name, email);

      JsonObject responseData = new JsonObject();
      responseData.addProperty("uid", uid);

      try {
        if (carpools.addUser(user)) {
          // User successfully added to database
          responseData.addProperty("isNewUser", true);
        } else {
          // User already exists in database
          responseData.addProperty("isNewUser", false);
        }

        responseData.addProperty("success", true);

      } catch (SQLException e) {
        responseData.addProperty("success", false);
        responseData.addProperty("error", e.getMessage());
      }

      return GSON.toJson(responseData);
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

  /**
   * Class to handle all page not found requests.
   */
  private static class ServerErrorHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request request, Response response) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Page Not Found")
          .put("favicon", "images/favicon.png").build();

      response.status(500);
      return new ModelAndView(variables, "server-error.ftl");
    }
  }
}
