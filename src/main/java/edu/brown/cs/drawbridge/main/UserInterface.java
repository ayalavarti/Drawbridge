package edu.brown.cs.drawbridge.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

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
   * Instantiates the resources for the analyzers and sets the end-points for
   * the front end GUI.
   */
  public static void setEndpoints() {
    FreeMarkerEngine freeMarker = createEngine();

    Spark.get("/", new HomeScreenHandler(), freeMarker);

  }

  /**
   * Handle requests to the home screen of the website.
   */
  private static class HomeScreenHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      // Return empty data to GUI when / route is called
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("title", "Drawbridge | Home")
          .put("favicon", "images/favicon.png").build();

      return new ModelAndView(variables, "map.ftl");
    }
  }

}
