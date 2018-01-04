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
import com.google.common.util.concurrent.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class FluentDecorator<V> extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V> implements FluentFuture<V> {

    private final Executor executor;

    FluentDecorator(ListenableFuture<V> future, Executor executor) {
        super(future);
        this.executor = executor;
    }

    FluentDecorator(ListenableFuture<V> future) {
        this(future, MoreExecutors.directExecutor());
    }

    @Override
    public <Y> FluentFuture<Y> transform(Executor executor, Function<V, Y> func) {
        return new FluentDecorator<>(Futures.transform(this, func, executor), this.executor);
    }

    @Override
    public <Y> FluentFuture<Y> transform(Executor executor, AsyncFunction<V, Y> func) {
        return new FluentDecorator<>(Futures.transformAsync(this, func, executor), this.executor);
    }

//    @Override
//    public FluentFuture<V> withFallback(FutureFallback<V> fallback) {
//        return new FluentDecorator<>(Futures.withFallback(this, fallback));
//    }
//
//    @Override
//    public FluentFuture<V> withFallback(Executor executor, FutureFallback<V> fallback) {
//        return new FluentDecorator<>(Futures.withFallback(this, fallback, executor), this.executor);
//    }

    @Override
    public FluentFuture<V> addCallback(Executor executor, FutureCallback<V> callback) {
        Futures.addCallback(this, callback, executor);
        return this;
    }

    @Override
    public FluentFuture<V> onSuccess(Executor executor, final Consumer<V> callback) {
        return addCallback(executor, ConsumerDecorator.success(callback));
    }

    @Override
    public FluentFuture<V> onFailure(Executor executor, final Consumer<Throwable> callback) {
        return addCallback(executor, ConsumerDecorator.<V>failure(callback));
    }

    @Override
    public <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func) {
        return new CheckedDecorator<>(Futures.makeChecked(this, func));
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
