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

import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;

class ConsumerDecorator<V> implements FutureCallback<V> {

    private final Optional<Consumer<V>> success;
    private final Optional<Consumer<Throwable>> failure;

    private ConsumerDecorator(Consumer<V> success, Consumer<Throwable> failure) {
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
        return new ConsumerDecorator<>(callback, null);
    }

    public static <V> FutureCallback<V> failure(Consumer<Throwable> callback) {
        return new ConsumerDecorator<>(null, callback);
    }

}
