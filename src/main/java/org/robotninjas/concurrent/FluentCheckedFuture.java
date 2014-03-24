package org.robotninjas.concurrent;

import com.google.common.util.concurrent.CheckedFuture;

public interface FluentCheckedFuture<V, X extends Exception>
    extends FluentFuture<V>, CheckedFuture<V, X>  {}
