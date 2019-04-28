package edu.brown.cs.drawbridge.main;

import edu.brown.cs.drawbridge.constants.Constants;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * The Main class of our project. This is where execution begins.
 *
 * @author Arvind Yalavarti
 */
public final class Main {

  private String[] args;

  /**
   * Method entrypoint for CLI invocation.
   *
   * @param args
   *     Arguments passed on the command line.
   */
  private Main(String[] args) {
    this.args = args;
  }

  /**
   * The initial method called when execution begins.
   *
   * @param args
   *     An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  /**
   * Processes the commands in a REPL.
   */
  private void run() {
    runSparkServer();
  }

  private void runSparkServer() {
    Spark.port(getHerokuAssignedPort());
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    // If we can't connect to the db, give up and stop the server
    if (UserInterface.setDB()) {
      UserInterface.setEndpoints();
    }
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return Constants.DEFAULT_PORT;
    //return default port if heroku-port isn't set (i.e. on localhost)
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
