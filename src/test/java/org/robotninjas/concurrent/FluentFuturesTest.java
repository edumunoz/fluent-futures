package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Callables;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static com.google.common.util.concurrent.MoreExecutors.shutdownAndAwaitTermination;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.robotninjas.concurrent.FluentExecutors.fluentDecorator;

public class FluentFuturesTest {

    private FluentExecutorService fluentExecutor;

    @Before
    public void setup() {
        fluentExecutor = fluentDecorator(newDirectExecutorService());
    }

    @After
    public void tearDown() {
        shutdownAndAwaitTermination(fluentExecutor, 10, SECONDS);
    }

    @Test
    public void testFluentFutureCreation() throws InterruptedException, ExecutionException {

        FluentFuture<?> ff1 = FluentFutures.from(new Exception());
        try {
            ff1.get();
            fail();
        } catch (ExecutionException e) {

        }

        FluentFuture<?> ff2 = FluentFutures.from(Futures.immediateFailedFuture(new Exception()));
        try {
            ff2.get();
            fail();
        } catch (ExecutionException e) {

        }

        FluentFuture<Integer> ff3 = FluentFutures.from(Futures.immediateFuture(1));
        Assert.assertEquals((Integer) 1, ff3.get());

        FluentFuture<Integer> ff4 = FluentFutures.from(1);
        Assert.assertEquals((Integer) 1, ff4.get());

    }

    @Test
    public void testTransform() throws ExecutionException, InterruptedException {

        FluentFuture<Double> ff = fluentExecutor
                .submit(Callables.returning(1))
                .transformAsync(MoreExecutors.newDirectExecutorService(), ToDoubleFunction.CONVERT);

        assertEquals((Double) 1.0, ff.get());

    }

    @Test(expected = CustomException.class)
    public void testCheckedFuture() throws CustomException {

        FluentCheckedFuture<Object, CustomException> cf = FluentFutures.from(1)
                .transformAsync(MoreExecutors.newDirectExecutorService(), ThrowingFunction.THROW)
                .makeChecked(ExceptionConverterFunction.CONVERT);

        cf.checkedGet();

    }

    private static enum ToDoubleFunction implements AsyncFunction<Integer, Double> {
        CONVERT;

        @Override
        public ListenableFuture<Double> apply(Integer input) throws Exception {
            return Futures.immediateFuture((double) input);
        }

    }

    private static enum ExceptionConverterFunction implements Function<Exception, CustomException> {
        CONVERT;

        @Override
        public CustomException apply(Exception input) {
            return new CustomException(input);
        }

    }

    private static enum ThrowingFunction implements AsyncFunction<Integer, Object> {
        THROW;

        @Override
        public ListenableFuture<Object> apply(Integer input) throws Exception {
            throw new Exception();
        }

    }

    private static class CustomException extends Exception {
        private CustomException(Throwable cause) {
            super(cause);
        }
    }

}
