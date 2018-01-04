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
import com.google.common.util.concurrent.CheckedFuture;
import com.google.common.util.concurrent.FutureCallback;
//import com.google.common.util.concurrent.FutureFallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class CheckedDecorator<V, X extends Exception> extends FluentDecorator<V> implements FluentCheckedFuture<V, X> {

    private final CheckedFuture<V, X> f;

    CheckedDecorator(CheckedFuture<V, X> future, Executor executor) {
        super(future, executor);
        this.f = future;
    }

    CheckedDecorator(CheckedFuture<V, X> future) {
        super(future);
        this.f = future;
    }

    @Override
    public <Y> FluentCheckedFuture<Y, X> transform(Executor executor, Function<? super V, ? extends Y> func) {
        return (FluentCheckedFuture<Y, X>) super.transform(executor, func);
    }

    @Override
    public <Y> FluentCheckedFuture<Y, X> transformAsync(Executor executor, AsyncFunction<? super V,? extends Y> func) {
        return (FluentCheckedFuture<Y, X>) super.transformAsync(executor, func);
    }

//    @Override
//    public FluentCheckedFuture<V, X> withFallback(FutureFallback<V> fallback) {
//        return (FluentCheckedFuture<V, X>) super.withFallback(fallback);
//    }
//
//    @Override
//    public FluentCheckedFuture<V, X> withFallback(Executor executor, FutureFallback<V> fallback) {
//        return (FluentCheckedFuture<V, X>) super.withFallback(executor, fallback);
//    }

    @Override
    public FluentCheckedFuture<V, X> addCallback(Executor executor, FutureCallback<V> callback) {
        return (FluentCheckedFuture<V, X>) super.addCallback(executor, callback);
    }

    @Override
    public FluentCheckedFuture<V, X> onSuccess(Executor executor, Consumer<V> callback) {
        return (FluentCheckedFuture<V, X>) super.onSuccess(executor, callback);
    }

    @Override
    public FluentCheckedFuture<V, X> onFailure(Executor executor, Consumer<Throwable> callback) {
        return (FluentCheckedFuture<V, X>) super.onFailure(executor, callback);
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
    public <Y extends Exception> V getChecked(TimeUnit timeUnit, Class<Y> exceptionClass, long l) throws Y {
        return super.getChecked(timeUnit, exceptionClass, l);
    }

    @Override
    public <E extends Exception> V getChecked(Class<E> exceptionClass) throws E {
        return super.getChecked(exceptionClass);
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
