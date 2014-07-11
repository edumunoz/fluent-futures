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

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

abstract class AbstractFluentExecutor extends AbstractExecutorService implements FluentExecutorService {

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return FluentFutureTask.create(runnable, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return FluentFutureTask.create(callable);
    }

    @Override
    public FluentFuture<?> submit(Runnable task) {
        return (FluentFuture<?>) super.submit(task);
    }

    @Override
    public <T> FluentFuture<T> submit(Runnable task, T result) {
        return (FluentFuture<T>) super.submit(task, result);
    }

    @Override
    public <T> FluentFuture<T> submit(Callable<T> task) {
        return (FluentFuture<T>) super.submit(task);
    }

}
