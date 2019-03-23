package edu.brown.cs.drawbridge.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.drawbridge.models.Trip;
import freemarker.template.Configuration;
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
 * @author Arvind Yalavarti
 *
 */
public abstract class UserInterface {
  private static final Gson GSON = new Gson();

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
   * Instantiates the resources for the analyzers and sets the end-points for
   * the front end GUI.
   */
  public static void setEndpoints() {
    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/", new HomeGetHandler(), freeMarker);

    Spark.get("/results", new ListGetHandler(), freeMarker);
    Spark.post("/results", new ListPostHandler(), freeMarker);

    Spark.get("/trip/<:tid>", new DetailGetHandler(), freeMarker);

    Spark.get("/my-trips/<:uid>", new UserGetHandler(), freeMarker);

    Spark.get("/new", new CreateGetHandler(), freeMarker);
    Spark.post("/new", new CreatePostHandler(), freeMarker);

    Spark.get("/help", new InfoGetHandler(), freeMarker);
  }

  //---------------------------- Home ------------------------------------
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
      return null;
    }
  }
  private static class ListPostHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      return null;
    }
  }

  //--------------------------- Detail -----------------------------------
  private static class DetailGetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response) {
      int tid;
      try {
        tid = Integer.parseInt(request.params(":tid"));
      } catch (NumberFormatException e) {
        response.redirect("/"); // 404 error
      }

      // Return empty data to GUI when / route is called
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "TRIP")
          .put("favicon", "images/favicon.png").build();
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
