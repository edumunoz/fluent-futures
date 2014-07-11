/*
 * (C) Copyright 2014 David Rusek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.robotninjas.concurrent;

import com.google.common.base.Function;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

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

    /**
     * Create a {@link org.robotninjas.concurrent.FluentFuture}
     *
     * @param value
     * @param executor
     * @param <Y>
     * @return
     */
    public static <Y> FluentFuture<Y> from(Y value, Executor executor) {
        return new FluentDecorator<>(Futures.immediateFuture(value), executor);
    }

    /**
     *
     * @param future
     * @param <Y>
     * @return
     */
    public static <Y> FluentFuture<Y> from(ListenableFuture<Y> future) {
        return new FluentDecorator<>(future);
    }

    /**
     *
     * @param future
     * @param executor
     * @param <Y>
     * @return
     */
    public static <Y> FluentFuture<Y> from(ListenableFuture<Y> future, Executor executor) {
        return new FluentDecorator<>(future, executor);
    }

    /**
     *
     * @param futures
     * @param <Y>
     * @return
     */
    @SafeVarargs
    public static <Y> FluentFuture<List<Y>> from(ListenableFuture<Y>... futures) {
        return new FluentDecorator<>(Futures.allAsList(Arrays.asList(futures)));
    }

    /**
     *
     * @param executor
     * @param futures
     * @param <Y>
     * @return
     */
    @SafeVarargs
    public static <Y> FluentFuture<List<Y>> from(Executor executor, ListenableFuture<Y>... futures) {
        return new FluentDecorator<>(Futures.allAsList(Arrays.asList(futures)), executor);
    }

    /**
     *
     * @param futures
     * @param <Y>
     * @return
     */
    public static <Y> FluentFuture<List<Y>> from(Iterable<ListenableFuture<Y>> futures) {
        return new FluentDecorator<>(Futures.allAsList(futures));
    }

    /**
     *
     * @param futures
     * @param executor
     * @param <Y>
     * @return
     */
    public static <Y> FluentFuture<List<Y>> from(Iterable<ListenableFuture<Y>> futures, Executor executor) {
        return new FluentDecorator<>(Futures.allAsList(futures), executor);
    }

    /**
     *
     * @param input1
     * @param input2
     * @param combine
     * @param executor
     * @param <X>
     * @param <Y>
     * @param <Z>
     * @return
     */
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

    /**
     *
     * @param input1
     * @param input2
     * @param combine
     * @param <X>
     * @param <Y>
     * @param <Z>
     * @return
     */
    public static <X, Y, Z> FluentFuture<Z> combine(ListenableFuture<X> input1, ListenableFuture<Y> input2,
                                                    Combine2<X, Y, Z> combine) {

        return combine(input1, input2, combine, MoreExecutors.sameThreadExecutor());
    }

}
