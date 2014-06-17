package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class CheckedWrapper<V, X extends Exception> extends FluentWrapper<V> implements FluentCheckedFuture<V, X> {

  private final CheckedFuture<V, X> f;

  CheckedWrapper(CheckedFuture<V, X> future, Executor executor) {
    super(future, executor);
    this.f = future;
  }

  CheckedWrapper(CheckedFuture<V, X> future) {
    super(future);
    this.f = future;
  }

  @Override
  public <Y> FluentCheckedFuture<Y, X> transform(Function<V, Y> func) {
    return (FluentCheckedFuture<Y, X>) super.transform(func);
  }

  @Override
  public <Y> FluentCheckedFuture<Y, X> transform(Executor executor, Function<V, Y> func) {
    return (FluentCheckedFuture<Y, X>) super.transform(executor, func);
  }

  @Override
  public <Y> FluentCheckedFuture<Y, X> transform(AsyncFunction<V, Y> func) {
    return (FluentCheckedFuture<Y, X>) super.transform(func);
  }

  @Override
  public <Y> FluentCheckedFuture<Y, X> transform(Executor executor, AsyncFunction<V, Y> func) {
    return (FluentCheckedFuture<Y, X>) super.transform(executor, func);
  }

  @Override
  public FluentCheckedFuture<V, X> withFallback(FutureFallback<V> fallback) {
    return (FluentCheckedFuture<V, X>) super.withFallback(fallback);
  }

  @Override
  public FluentCheckedFuture<V, X> withFallback(Executor executor, FutureFallback<V> fallback) {
    return (FluentCheckedFuture<V, X>) super.withFallback(executor, fallback);
  }

  @Override
  public FluentCheckedFuture<V, X> addCallback(FutureCallback<V> callback) {
    return (FluentCheckedFuture<V, X>) super.addCallback(callback);
  }

  @Override
  public FluentCheckedFuture<V, X> addCallback(Executor executor, FutureCallback<V> callback) {
    return (FluentCheckedFuture<V, X>) super.addCallback(executor, callback);
  }

  @Override
  public FluentCheckedFuture<V, X> onSuccess(Executor executor, Consumer<V> callback) {
    return (FluentCheckedFuture<V, X>) super.onSuccess(executor, callback);
  }

  @Override
  public FluentCheckedFuture<V, X> onSuccess(Consumer<V> callback) {
    return (FluentCheckedFuture<V, X>) super.onSuccess(callback);
  }

  @Override
  public FluentCheckedFuture<V, X> onFailure(Executor executor, Consumer<Throwable> callback) {
    return (FluentCheckedFuture<V, X>) super.onFailure(executor, callback);
  }

  @Override
  public FluentCheckedFuture<V, X> onFailure(Consumer<Throwable> callback) {
    return (FluentCheckedFuture<V, X>) super.onFailure(callback);
  }

  @Override
  public <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func) {
    return super.makeChecked(func);
  }

  @Override
  public V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
    return super.get(l, timeUnit);
  }

  @Override
  public <Y extends Exception> V get(long l, TimeUnit timeUnit, Class<Y> exceptionClass) throws Y{
    return super.get(l, timeUnit, exceptionClass);
  }

  @Override
  public <E extends Exception> V get(Class<E> exceptionClass) throws E{
    return super.get(exceptionClass);
  }

  @Override
  public V checkedGet() throws X {
    return f.checkedGet();
  }

  @Override
  public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, X {
    return f.checkedGet(timeout, unit);
  }
}
