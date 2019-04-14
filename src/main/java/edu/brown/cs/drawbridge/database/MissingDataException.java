package edu.brown.cs.drawbridge.database;

/**
 * A class for exception types that indicate corrupted data in a database.
 *
 * @author Sam Maffa
 */
public class MissingDataException extends Exception {

  private static final long serialVersionUID = 4638653630293174225L;

  /**
   * The default constructor. Contains no error message.
   */
  public MissingDataException() {
    super();
  }

  /**
   * A Constructor that accepts a custom error message.
   *
   * @param message
   *     A String message that explains the error.
   */
  public MissingDataException(String message) {
    super("ERROR: " + message);
  }

}
