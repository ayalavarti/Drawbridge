package edu.brown.cs.drawbridge.util;

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

  public Pair(T first, U second) {
    this.first = first;
    this.second = second;
  }

  public T getFirst() {
    return first;
  }

  public U getSecond() {
    return second;
  }
}
