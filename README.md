This is my attempt to provide a fluent future for Java (mainly java6 and java7 as java8 has `CompletableFuture<V>`)

The base type included here is `FluentFuture<V>` which builds on Guava's `ListenableFuture<V>` and adds methods to make the operations under Guava's `Futures` utility class members. Most of the existing types and interfaces are preserved, as is, from Guava and a few new ones have been added where it makes sense.

Get it:

```xml
    <groupId>org.robotninjas</groupId>
    <artifactId>fluent-futures</artifactId>
    <version>1.1-SNAPSHOT</version>
```

Basic interface

```java
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
```

You can obtain a `FluentFuture<V>` either from an existing `ListenableFuture<V>`

```java
ListenableFuture<Object> listenable = ...
FluentFuture<Object> fluent = FluentFutures.from(listenable);
```

Or, you can use an `ExecutorService` that provides FluentFutures

```java
ExecutorService executor = ...
FluentExecutorService fluentExecutor = FluentExecutors.fluentDecorator(executor);
```

In addition, methods that didn't make sense as members, such as `Futures.allAsList()`, `Futures.successfulAsList()`, and factories for immediate values, exist under `FluentFutures`.

You may then operate on the future:

```java
fluentExecutor.submit(new Callable<Object>() {
  @Override
  public Object call() throws Exception {
    return null;
  }
}).transform(new AsyncFunction<Object, Object>() {
  @Override
  public ListenableFuture<Object> apply(Object input) throws Exception {
    return null;
  }
}).onSuccess(new Consumer<Object>() {
  @Override
  public void consume(@Nullable Object value) {

  }
}).onFailure(new Consumer<Throwable>() {
  @Override
  public void consume(@Nullable Throwable value) {

  }
});
```

You may also convert a `FluentFuture<V>` to a 'FluentCheckedFuture<V, E extends Exception>`

```java
FluentCheckedFuture<Object, CustomException> checked =
  fluent.makeChecked(new Function<Exception, CustomException>() {
    @Override
    public CustomException apply(Exception input) {
      return null;
    }
  });
```

You can also combine `FluentFuture`s

```java
FluentFuture<Integer> f1 = ...
FluentFuture<Double> f2 = ...
FluentFuture<Combined> combined = FluentFutures.combine(f1, f2, new Combine2() {
  public Combined combine(Integer left, Double right) {
    return new Combined(left, right);
  });

combined.get();
```

TODO: FluentScheduledExecutorService and combine more than two futures
