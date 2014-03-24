package org.robotninjas.concurrent;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;

class ConsumerWrapper<V> implements FutureCallback<V> {

  private final Optional<Consumer<V>> success;
  private final Optional<Consumer<Throwable>> failure;

  private ConsumerWrapper(Consumer<V> success, Consumer<Throwable> failure) {
    this.success = Optional.fromNullable(success);
    this.failure = Optional.fromNullable(failure);
  }

  @Override
  public void onSuccess(V result) {
    if (success.isPresent()) {
      success.get().consume(result);
    }
  }

  @Override
  public void onFailure(Throwable t) {
    if (failure.isPresent()) {
      failure.get().consume(t);
    }
  }

  public static <V> FutureCallback<V> success(Consumer<V> callback) {
    return new ConsumerWrapper<>(callback, null);
  }

  public static <V> FutureCallback<V> failure(Consumer<Throwable> callback) {
    return new ConsumerWrapper<>(null, callback);
  }

}
