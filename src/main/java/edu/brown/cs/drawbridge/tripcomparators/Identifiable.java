package edu.brown.cs.drawbridge.tripcomparators;

/**
 * An interface for classes that require an identified user.
 */
public interface Identifiable {

  /**
   * Set the id of the User, used to compare searched lists of Trips.
   *
   * @param userId
   *          The id of the User used to compare searched lists of Trips
   */
  void setUserId(String userId);

}
