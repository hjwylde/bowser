package com.hjwylde.bowser.modules;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Provides an {@link ExecutorService} for use on background threads. If this application were larger it would be better
 * to manually manage the lifecycle of the executor service, however for a small one this executor is just shared and
 * managed by the {@link com.hjwylde.bowser.ui.frames.bowser.BowserFrame}.
 */
public final class ExecutorServiceModule {
    private static final @NotNull ExecutorService INSTANCE = createInstance();

    public static @NotNull ExecutorService provideExecutorService() {
        return INSTANCE;
    }

    private static @NotNull ExecutorService createInstance() {
        // Effectively a cached thread pool (see Executors) with a DiscardPolicy
        return new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }
}
