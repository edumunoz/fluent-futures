package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class FluentWrapper<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements FluentFuture<V> {

  private final Executor executor;

  FluentWrapper(ListenableFuture<V> future, Executor executor) {
    super(future);
    this.executor = executor;
  }

  FluentWrapper(ListenableFuture<V> future) {
    this(future, MoreExecutors.sameThreadExecutor());
  }

  @Override
  public <Y> FluentFuture<Y> transform(Function<V, Y> func) {
    return new FluentWrapper<>(Futures.transform(this, func));
  }

  @Override
  public <Y> FluentFuture<Y> transform(Executor executor, Function<V, Y> func) {
    return new FluentWrapper<>(Futures.transform(this, func, executor), this.executor);
  }

  @Override
  public <Y> FluentFuture<Y> transform(AsyncFunction<V, Y> func) {
    return new FluentWrapper<>(Futures.transform(this, func));
  }

  @Override
  public <Y> FluentFuture<Y> transform(Executor executor, AsyncFunction<V, Y> func) {
    return new FluentWrapper<>(Futures.transform(this, func, executor), this.executor);
  }

  @Override
  public FluentFuture<V> withFallback(FutureFallback<V> fallback) {
    return new FluentWrapper<>(Futures.withFallback(this, fallback));
  }

  @Override
  public FluentFuture<V> withFallback(Executor executor, FutureFallback<V> fallback) {
    return new FluentWrapper<>(Futures.withFallback(this, fallback, executor), this.executor);
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
    return addCallback(executor, ConsumerWrapper.success(callback));
  }

  @Override
  public FluentFuture<V> onSuccess(final Consumer<V> callback) {
    return onSuccess(MoreExecutors.sameThreadExecutor(), callback);
  }

  @Override
  public FluentFuture<V> onFailure(Executor executor, final Consumer<Throwable> callback) {
    return addCallback(executor, ConsumerWrapper.<V>failure(callback));
  }

  @Override
  public FluentFuture<V> onFailure(final Consumer<Throwable> callback) {
    return onFailure(MoreExecutors.sameThreadExecutor(), callback);
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
  public <E extends Exception> V get(Class<E> exceptionClass) throws E {
    return Futures.get(this, exceptionClass);
  }

}
