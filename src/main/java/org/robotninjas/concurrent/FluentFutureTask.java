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
    return new FluentWrapper<>(Futures.transform(this, func));
  }

  @Override
  public <Y> FluentFuture<Y> transform(Function<V, Y> func, Executor executor) {
    return new FluentWrapper<>(Futures.transform(this, func, executor));
  }

  @Override
  public <Y> FluentFuture<Y> transform(AsyncFunction<V, Y> func) {
    return new FluentWrapper<>(Futures.transform(this, func));
  }

  @Override
  public <Y> FluentFuture<Y> transform(AsyncFunction<V, Y> func, Executor executor) {
    return new FluentWrapper<>(Futures.transform(this, func, executor));
  }

  @Override
  public FluentFuture<V> withFallback(FutureFallback<V> fallback) {
    return new FluentWrapper<>(Futures.withFallback(this, fallback));
  }

  @Override
  public FluentFuture<V> withFallback(FutureFallback<V> fallback, Executor executor) {
    return new FluentWrapper<>(Futures.withFallback(this, fallback, executor));
  }

  @Override
  public FluentFuture<V> addCallback(FutureCallback<V> callback) {
    Futures.addCallback(this, callback);
    return this;
  }

  @Override
  public FluentFuture<V> addCallback(FutureCallback<V> callback, Executor executor) {
    Futures.addCallback(this, callback, executor);
    return this;
  }

  @Override
  public FluentFuture<V> onSuccess(final Consumer<V> callback, Executor executor) {
    return addCallback(ConsumerWrapper.success(callback), executor);
  }

  @Override
  public FluentFuture<V> onSuccess(final Consumer<V> callback) {
    return onSuccess(callback, MoreExecutors.sameThreadExecutor());
  }

  @Override
  public FluentFuture<V> onFailure(final Consumer<Throwable> callback, Executor executor) {
    return addCallback(ConsumerWrapper.<V>failure(callback), executor);
  }

  @Override
  public FluentFuture<V> onFailure(final Consumer<Throwable> callback) {
    return onFailure(callback, MoreExecutors.sameThreadExecutor());
  }

  @Override
  public <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func) {
    return new CheckedWrapper<>(Futures.makeChecked(this, func));
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
