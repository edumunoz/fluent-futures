package org.robotninjas.concurrent;

public interface Combine2<X, Y, Z> {
  Z combine(X input1, Y input2);
}

