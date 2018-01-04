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
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface FluentFuture<V> extends ListenableFuture<V> {
    <Y> FluentFuture<Y> transform(Executor executor, Function<? super V, ? extends Y> func);

    <Y> FluentFuture<Y> transformAsync(Executor executor, AsyncFunction<? super V, ? extends Y> func);

//    FluentFuture<V> withFallback(FutureFallback<V> fallback);
//
//    FluentFuture<V> withFallback(Executor executor, FutureFallback<V> fallback);

    FluentFuture<V> addCallback(Executor executor, FutureCallback<V> callback);

    FluentFuture<V> onSuccess(Executor executor, Consumer<V> callback);

    FluentFuture<V> onFailure(Executor executor, Consumer<Throwable> callback);

    <E extends Exception> FluentCheckedFuture<V, E> makeChecked(Function<Exception, E> func);

    V get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException;

    <X extends Exception> V getChecked(TimeUnit timeUnit, Class<X> exceptionClass, long l) throws X;

    <E extends Exception> V getChecked(Class<E> exceptionClass) throws E;

}
