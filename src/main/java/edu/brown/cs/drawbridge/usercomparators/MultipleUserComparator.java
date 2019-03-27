package edu.brown.cs.drawbridge.usercomparators;

import java.util.Comparator;
import java.util.List;

import edu.brown.cs.drawbridge.models.User;

/**
 * Comparator to compare two Users using a list of comparators. If the first
 * compare method returns 0, then use the next comparator.
 */
public class MultipleUserComparator implements Comparator<User> {

  private List<ComparesUsersInTrip> comparators;

  /**
   * MultipleComparator constructor.
   *
   * @param comparators
   *          List of comparators used to compare two Users in the same Trip
   */
  public MultipleUserComparator(List<ComparesUsersInTrip> comparators) {
    this.comparators = comparators;
  }

  @Override
  public int compare(User user1, User user2) {
    for (int i = 0; i < comparators.size(); i++) {
      int currentRanking = comparators.get(i).compare(user1, user2);
      // If a comparator has sorted the two
      if (currentRanking != 0) {
        return currentRanking;
      }
      // Otherwise, loop to the next comparator
    }
    return 0;
  }
}
