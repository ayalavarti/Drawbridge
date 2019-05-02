package edu.brown.cs.drawbridge.util;

import java.util.Objects;

/**
 * Immutable pair class for carrying two things.
 * @param <T> The type of the first value in the pair.
 * @param <U> The type of the second value in the pair.
 *
 * @author Mark Lavrentyev
 */
public class Pair<T, U> {
  private final T first;
  private final U second;

  /**
   * Creates an immutable pair object.
   * @param first The first object in the pair.
   * @param second The second object in the pair.
   */
  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  /**
   * Gets the first object in the pair.
   * @return The first object in the pair.
   */
  public T getFirst() {
    return first;
  }

  /**
   * Gets the second object in the pair.
   * @return The second object in the pair.
   */
  public U getSecond() {
    return second;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Pair)) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(first, pair.first)
           && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
