package org.robotninjas.concurrent;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class FluentExecutors {

  public static FluentExecutorService fluentDecorator(ExecutorService executorService) {
    return new FluentDecorator(executorService);
  }

  static class FluentDecorator extends AbstractFluentExecutor {

    private final ExecutorService delegate;

    public FluentDecorator(ExecutorService executor) {
      this.delegate = executor;
    }

    @Override
    public void shutdown() {
      delegate.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
      return delegate.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
      return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
      return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
      delegate.execute(command);
    }

  }

}
