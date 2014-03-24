package org.robotninjas.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public interface FluentExecutorService extends ExecutorService {

  @Override
  <T> FluentFuture<T> submit(Callable<T> task);

  @Override
  <T> FluentFuture<T> submit(Runnable task, T result);

  @Override
  FluentFuture<?> submit(Runnable task);

}
