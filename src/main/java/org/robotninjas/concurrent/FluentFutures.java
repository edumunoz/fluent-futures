package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class FluentFutures {

  /**
   * Create a {FluentFuture} from a static value
   *
   * @param value the value
   * @param <Y>   the type of the value
   * @return a new completed future
   */
  public static <Y> FluentFuture<Y> from(Y value) {
    return new FluentDecorator<>(Futures.immediateFuture(value));
  }

  /**
   * Create a {FluentFuture} from an exception
   *
   * @param exception the exception
   * @param <Y>       the type for compatability
   * @return a new exceptional future
   */
  public static <Y> FluentFuture<Y> from(Exception exception) {
    return new FluentDecorator<>(Futures.<Y>immediateFailedFuture(exception));
  }

  public static <Y> FluentFuture<Y> from(Y value, Executor executor) {
    return new FluentDecorator<>(Futures.immediateFuture(value), executor);
  }

  public static <Y> FluentFuture<Y> from(ListenableFuture<Y> future) {
    return new FluentDecorator<>(future);
  }

  public static <Y> FluentFuture<Y> from(ListenableFuture<Y> future, Executor executor) {
    return new FluentDecorator<>(future, executor);
  }

  @SafeVarargs
  public static <Y> FluentFuture<List<Y>> from(ListenableFuture<Y>... futures) {
    return new FluentDecorator<>(Futures.allAsList(Arrays.asList(futures)));
  }

  @SafeVarargs
  public static <Y> FluentFuture<List<Y>> from(Executor executor, ListenableFuture<Y>... futures) {
    return new FluentDecorator<>(Futures.allAsList(Arrays.asList(futures)), executor);
  }

  public static <Y> FluentFuture<List<Y>> from(Iterable<ListenableFuture<Y>> futures) {
    return new FluentDecorator<>(Futures.allAsList(futures));
  }

  public static <Y> FluentFuture<List<Y>> from(Iterable<ListenableFuture<Y>> futures, Executor executor) {
    return new FluentDecorator<>(Futures.allAsList(futures), executor);
  }

  public static <X, Y, Z> FluentFuture<Z> combine(ListenableFuture<X> input1, final ListenableFuture<Y> input2,
                                                  final Combine2<X, Y, Z> combine, Executor executor) {

    return FluentFutures.from(Futures.transform(input1, new AsyncFunction<X, Z>() {
      public ListenableFuture<Z> apply(final X left) throws Exception {
        return Futures.transform(input2, new Function<Y, Z>() {
          public Z apply(Y right) {
            return combine.combine(left, right);
          }
        });
      }
    }, executor));
  }

  public static <X, Y, Z> FluentFuture<Z> combine(ListenableFuture<X> input1, ListenableFuture<Y> input2,
                                                  Combine2<X, Y, Z> combine) {

    return combine(input1, input2, combine, MoreExecutors.sameThreadExecutor());
  }

}
