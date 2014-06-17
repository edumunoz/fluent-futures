package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

public interface FluentFuture<V> extends ListenableFuture<V> {

  <Y> FluentFuture<Y> transform(Function<V, Y> func);

  <Y> FluentFuture<Y> transform(Executor executor, Function<V, Y> func);

  <Y> FluentFuture<Y> transform(AsyncFunction<V, Y> func);

  <Y> FluentFuture<Y> transform(Executor executor, AsyncFunction<V, Y> func);

  FluentFuture<V> withFallback(FutureFallback<V> fallback);

  FluentFuture<V> withFallback(Executor executor, FutureFallback<V> fallback);

  FluentFuture<V> addCallback(FutureCallback<V> callback);

  FluentFuture<V> addCallback(Executor executor, FutureCallback<V> callback);

  FluentFuture<V> onSuccess(Consumer<V> callback);

  FluentFuture<V> onSuccess(Executor executor, Consumer<V> callback);

  FluentFuture<V> onFailure(Consumer<Throwable> callback);

  FluentFuture<V> onFailure(Executor executor, Consumer<Throwable> callback);

  <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func);

  V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException;

  <X extends Exception> V get(long l, TimeUnit timeUnit, Class<X> exceptionClass) throws X;

  <E extends Exception> V get(Class<E> exceptionClass) throws E;

}
