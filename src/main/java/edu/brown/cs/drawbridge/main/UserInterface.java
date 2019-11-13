package edu.brown.cs.drawbridge.main;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.brown.cs.drawbridge.carpools.Carpools;
import edu.brown.cs.drawbridge.carpools.TripCleaner;
import edu.brown.cs.drawbridge.constants.Constants;
import edu.brown.cs.drawbridge.database.MissingDataException;
import edu.brown.cs.drawbridge.json.JSONProcessor;
import edu.brown.cs.drawbridge.models.Trip;
import edu.brown.cs.drawbridge.models.User;
import edu.brown.cs.drawbridge.util.Pair;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * An abstract class for the User Interface of the Java project. Contains
 * Handler objects for each end-point defined in the setEnpoints() method.
 *
 * @author Mark Lavrentyev
 */
public final class UserInterface {

  private static final Gson GSON = new Gson();
  private static final String MAPBOX_TOKEN = System.getenv("MAPBOX_KEY");
  private static final String GOOGLE_CLIENT_ID = System
      .getenv("GOOGLE_CLIENT_ID");
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
   * @return true when the set is successful; false when unsuccessful.
   */
  public static boolean setDB() {
    try {
      URI dbURI = new URI(System.getenv("DATABASE_URL"));
      String username = dbURI.getUserInfo().split(":")[0];
      String password = dbURI.getUserInfo().split(":")[1];

      String dbURL = "jdbc:postgresql://" + dbURI.getHost() + ':' + dbURI
          .getPort() + dbURI.getPath();

      carpools = new Carpools(dbURL, username, password);

      //Set up cron job to clean out old trips periodically
      Timer timer = new Timer();
      TimerTask tripCleaner = new TripCleaner(carpools);
      timer.scheduleAtFixedRate(tripCleaner, 0, Constants.TRIP_CLEAN_INTERVAL);

      return true;
    } catch (URISyntaxException e) {
      System.out.println(
          "ERROR: Problem reading database url location: " + e.getMessage());
      return false;
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(
          "ERROR: Could not connect to the database: " + e.getMessage());
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
    Spark.post("/results", new ListPostHandler());

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
  // -------------------------- Helpers -----------------------------------

  private static Pair<JsonObject, JsonArray> executeSearch(QueryParamsMap qm) {
    // Get parameter values
    JsonArray data;
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

      payload.addProperty("startName", startName);
      payload.addProperty("endName", endName);
      payload.addProperty("startLat", startLat);
      payload.addProperty("startLon", startLon);
      payload.addProperty("endLat", endLat);
      payload.addProperty("endLon", endLon);
      payload.addProperty("date", datetime);

      double eta = Double.parseDouble(qm.value("eta"));
      double tripDist = Double.parseDouble(qm.value("tripDist"));

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
      payload.addProperty("walkTime", walkTime);
      payload.addProperty("waitTime", waitTime);
      payload.addProperty("eta", eta);
      payload.addProperty("tripDist", tripDist);

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
      if (uid != null) {
        data = JSONProcessor.processTripGroups(uid, results);
      } else {
        data = JSONProcessor.processTripGroups(results);
      }

    } catch (RuntimeException | SQLException | MissingDataException e) {
      e.printStackTrace();
      data = new JsonArray();
    }

    return new Pair<>(payload, data);
  }

  // ---------------------------- Home ------------------------------------

  /**
   * Handle requests to the home screen of the website.
   */
  private static class HomeGetHandler implements TemplateViewRoute {
    @Override public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Home").put("mapboxKey", MAPBOX_TOKEN)
          .put("google_client_id", GOOGLE_CLIENT_ID)
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

      Pair<JsonObject, JsonArray> searchResults = executeSearch(
          request.queryMap());

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Results")
          .put("favicon", "images/favicon.png")
          .put("google_client_id", GOOGLE_CLIENT_ID)
          .put("data", GSON.toJson(searchResults.getSecond()))
          .put("query", GSON.toJson(searchResults.getFirst())).build();

      return new ModelAndView(variables, "results.ftl");
    }
  }

  /**
   * Handler for checking trips for membership after they're displayed to
   * display accurate hosting/pending/member information if relevant.
   */
  private static class ListPostHandler implements Route {
    @Override public Object handle(Request request, Response response) {

      Pair<JsonObject, JsonArray> searchResults = executeSearch(
          request.queryMap());
      JsonObject returnData = new JsonObject();
      returnData.add("payload", searchResults.getFirst());
      returnData.add("data", searchResults.getSecond());

      return GSON.toJson(returnData);
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
            .put("google_client_id", GOOGLE_CLIENT_ID)
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
          .put("google_client_id", GOOGLE_CLIENT_ID).put("pending", pending)
          .build();
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
          success |= carpools.rejectRequest(tid, uid, uid);

        } else if (action.equals("delete")) {
          success = carpools.deleteTrip(tid, uid);
          responseData.addProperty("redirect", "/my-trips");

        } else if (action.equals("approve")) {
          String pendingUID = qm.value("pendingUser");
          success = carpools.approveRequest(tid, uid, pendingUID);

        } else if (action.equals("deny")) {
          String pendingUID = qm.value("pendingUser");
          success = carpools.rejectRequest(tid, uid, pendingUID);
        } else if (action.equals("editComment")) {
          String newComment = qm.value("newComment");
          success = carpools.updateComment(tid, uid, newComment);
          responseData.addProperty("newComment", newComment);
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
          .put("google_client_id", GOOGLE_CLIENT_ID)
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

        return GSON.toJson(
            JSONProcessor.processTripGroups(uid, hosting, member, pending));
      } catch (SQLException | MissingDataException e) {
        // TODO: decide stuff to do
        return GSON.toJson(new JsonObject());
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
          .put("google_client_id", GOOGLE_CLIENT_ID)
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
      long eta = (long) (Double.parseDouble(qm.value("eta")));
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
      String profilePic = qm.value("profilePic");

      User user = new User(uid, name, email, profilePic);

      JsonObject responseData = new JsonObject();
      responseData.addProperty("uid", uid);

      try {
        // Check if they're already a member and add them if they're not
        if (carpools.addUser(user)) {
          // User successfully added to database
          responseData.addProperty("isNewUser", true);
        } else {
          // User already exists in database
          responseData.addProperty("isNewUser", false);
        }

        // Update the user's profile picture
        carpools.updateProfilePic(uid, profilePic);

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
          .put("google_client_id", GOOGLE_CLIENT_ID)
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
          .put("google_client_id", GOOGLE_CLIENT_ID)
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
          .put("google_client_id", GOOGLE_CLIENT_ID)
          .put("favicon", "images/favicon.png").build();

      response.status(500);
      return new ModelAndView(variables, "server-error.ftl");
    }
  }
}
