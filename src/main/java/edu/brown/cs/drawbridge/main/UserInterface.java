package edu.brown.cs.drawbridge.main;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;

import freemarker.template.Configuration;
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
  }
}
