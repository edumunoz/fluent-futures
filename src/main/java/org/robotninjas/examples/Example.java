package org.robotninjas.examples;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.robotninjas.concurrent.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example {

  public static void main(String... args) throws InterruptedException {

    ExecutorService loggingExecutor = Executors.newSingleThreadExecutor();
    ExecutorService eventExecutor = Executors.newCachedThreadPool();
    FluentExecutorService fluentExecutor = FluentExecutors.fluentDecorator(eventExecutor);

    try {

      System.out.println("1");

      FluentFuture<Double> f1 = fluentExecutor
          .submit(Callables.returning(1))
          .transform(ToDouble.CONVERT)
          .onSuccess(loggingExecutor, SuccessLogger.LOG)
          .onFailure(loggingExecutor, FailureLogger.LOG);

      System.out.println("2");

      FluentFuture<Double> f2 = fluentExecutor
          .submit(Callables.returning(2))
          .transform(ToDouble.CONVERT)
          .onSuccess(loggingExecutor, SuccessLogger.LOG)
          .onFailure(loggingExecutor, FailureLogger.LOG);

      System.out.println("3");

      FluentCheckedFuture<Double, CustomException> result = FluentFutures
          .combine(f1, f2, DoubleCombine.COMBINE)
          .onSuccess(loggingExecutor, SuccessLogger.LOG)
          .onFailure(loggingExecutor, FailureLogger.LOG)
          .makeChecked(CheckedConverter.CONVERT);

      System.out.println(result.checkedGet());

    } catch (CustomException e) {

      Throwables.propagate(e);

    } finally {

      fluentExecutor.shutdown();
      loggingExecutor.shutdown();

    }

  }

  private static enum ToDouble implements AsyncFunction<Integer, Double> {
    CONVERT;

    @Override
    public ListenableFuture<Double> apply(Integer input) throws Exception {
      return Futures.immediateFuture((double) input);
    }

  }

  private static enum DoubleCombine implements Combine2<Double, Double, Double> {
    COMBINE;

    @Override
    public Double combine(Double input1, Double input2) {
      return input1 + input2;
    }
  }

  private static enum SuccessLogger implements Consumer<Double> {
    LOG;

    @Override
    public void consume(Double value) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(value);
    }
  }

  private static enum FailureLogger implements Consumer<Throwable> {
    LOG;

    @Override
    public void consume(Throwable value) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      value.printStackTrace();
    }
  }

  private static enum CheckedConverter implements Function<Exception, CustomException> {
    CONVERT;

    @Override
    public CustomException apply(Exception input) {
      return new CustomException(input);
    }

  }

  private static class CustomException extends Exception {
    private CustomException(Throwable cause) {
      super(cause);
    }
  }

}
