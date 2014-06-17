package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

/**
 * A {FutureTask<V>} which implements {FluentFuture<V>}
 *
 * @see java.util.concurrent.FutureTask
 * @see org.robotninjas.concurrent.FluentFuture
 *
 * @param <V> The result type returned by this FluentFutureTask's <tt>get</tt> method
 */
public class FluentFutureTask<V> extends FutureTask<V> implements FluentFuture<V>, RunnableFuture<V> {

  private final ExecutionList executionList = new ExecutionList();

  FluentFutureTask(Callable<V> callable) {
    super(callable);
  }

  FluentFutureTask(Runnable runnable, V result) {
    super(runnable, result);
  }

  public static <V> FluentFutureTask<V> create(Callable<V> callable) {
    return new FluentFutureTask<V>(callable);
  }

  public static <V> FluentFutureTask<V> create(Runnable runnable, V result) {
    return new FluentFutureTask<>(runnable, result);
  }

  @Override
  public <Y> FluentFuture<Y> transform(Function<V, Y> func) {
    return new FluentDecorator<>(Futures.transform(this, func));
  }

  @Override
  public <Y> FluentFuture<Y> transform(Executor executor, Function<V, Y> func) {
    return new FluentDecorator<>(Futures.transform(this, func, executor));
  }

  @Override
  public <Y> FluentFuture<Y> transform(AsyncFunction<V, Y> func) {
    return new FluentDecorator<>(Futures.transform(this, func));
  }

  @Override
  public <Y> FluentFuture<Y> transform(Executor executor, AsyncFunction<V, Y> func) {
    return new FluentDecorator<>(Futures.transform(this, func, executor));
  }

  @Override
  public FluentFuture<V> withFallback(FutureFallback<V> fallback) {
    return new FluentDecorator<>(Futures.withFallback(this, fallback));
  }

  @Override
  public FluentFuture<V> withFallback(Executor executor, FutureFallback<V> fallback) {
    return new FluentDecorator<>(Futures.withFallback(this, fallback, executor));
  }

  @Override
  public FluentFuture<V> addCallback(FutureCallback<V> callback) {
    Futures.addCallback(this, callback);
    return this;
  }

  @Override
  public FluentFuture<V> addCallback(Executor executor, FutureCallback<V> callback) {
    Futures.addCallback(this, callback, executor);
    return this;
  }

  @Override
  public FluentFuture<V> onSuccess(Executor executor, final Consumer<V> callback) {
    return addCallback(executor, ConsumerDecorator.success(callback));
  }

  @Override
  public FluentFuture<V> onSuccess(final Consumer<V> callback) {
    return onSuccess(MoreExecutors.sameThreadExecutor(), callback);
  }

  @Override
  public FluentFuture<V> onFailure(Executor executor, final Consumer<Throwable> callback) {
    return addCallback(executor, ConsumerDecorator.<V>failure(callback));
  }

  @Override
  public FluentFuture<V> onFailure(final Consumer<Throwable> callback) {
    return onFailure(MoreExecutors.sameThreadExecutor(), callback);
  }

  @Override
  public <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func) {
    return new CheckedDecorator<>(Futures.makeChecked(this, func));
  }

  @Override
  public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return this.get(l, timeUnit);
  }

  @Override
  public <X extends Exception> V get(long l, TimeUnit timeUnit, Class<X> exceptionClass) throws X {
    return Futures.get(this, l, timeUnit, exceptionClass);
  }

  @Override
  public <Y extends Exception> V get(Class<Y> exceptionClass) throws Y {
    return Futures.get(this, exceptionClass);
  }

  @Override
  public void addListener(Runnable listener, Executor executor) {
    executionList.add(listener, executor);
  }

  @Override
  protected void done() {
    executionList.execute();
  }

}
