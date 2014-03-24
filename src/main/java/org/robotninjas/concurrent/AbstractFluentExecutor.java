package org.robotninjas.concurrent;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

abstract class AbstractFluentExecutor extends AbstractExecutorService implements FluentExecutorService {

  @Override
  protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return FluentFutureTask.create(runnable, value);
  }

  @Override
  protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    return FluentFutureTask.create(callable);
  }

  @Override
  public FluentFuture<?> submit(Runnable task) {
    return (FluentFuture<?>) super.submit(task);
  }

  @Override
  public <T> FluentFuture<T> submit(Runnable task, T result) {
    return (FluentFuture<T>) super.submit(task, result);
  }

  @Override
  public <T> FluentFuture<T> submit(Callable<T> task) {
    return (FluentFuture<T>) super.submit(task);
  }

}
