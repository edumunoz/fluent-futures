package org.robotninjas.concurrent;

public interface Consumer<V> {
  void consume(V value);
}
