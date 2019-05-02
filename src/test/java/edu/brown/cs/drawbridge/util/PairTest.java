package edu.brown.cs.drawbridge.util;

import org.junit.Test;

/**
 * Tests the pair class.
 * @author Mark Lavrentyev
 */
public class PairTest {

  @Test
  public void testGetters() {
    for (int i = -100; i < 100; i++) {
      for (int j = -100; j < 100; j++) {
        Pair<Integer, Integer> pair = new Pair<>(i, j);
        assert pair.getFirst().equals(i);
        assert pair.getSecond().equals(j);
      }
    }
  }

  @Test
  public void testDifferentTypes() {
    Pair<String, Integer> p1 = new Pair<>("hello", 2);
    Pair<Integer, String> p2 = new Pair<>(2, "hello");

    assert p1.getFirst().equals("hello");
    assert p1.getSecond().equals(2);
    assert p2.getFirst().equals(2);
    assert p2.getSecond().equals("hello");
  }

  @Test
  public void testEquals() {
    Pair<String, Integer> p1 = new Pair<>("hello", 2);
    Pair<Integer, String> p2 = new Pair<>(2, "hello");
    Pair<String, Integer> p3 = new Pair<>("hello", -2);
    Pair<String, Integer> p4 = new Pair<>("goodbye", 2);
    Pair<String, Integer> p5 = new Pair<>("hello", 2);

    assert p1.equals(p5) && p5.equals(p1);
    assert !p1.equals(p2);
    assert !p1.equals(p3);
    assert !p1.equals(p4);
  }
}
