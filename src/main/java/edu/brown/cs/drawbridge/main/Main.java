package edu.brown.cs.drawbridge.main;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
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

  private static final int DEFAULT_PORT = 4567;
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
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    // Accept gui command and begin REPL reading
    try {
      OptionSet options = parser.parse(args);
      if (options.has("gui")) {
        runSparkServer((int) options.valueOf("port"));
      }
    } catch (OptionException e) {
      System.out.println("Use one of: --gui [--port <number>] or no inputs.");
    }
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    // If we can't connect to the db, give up and stop the server
    if (UserInterface.setDB("carpools")) {
      UserInterface.setEndpoints();
    }
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
