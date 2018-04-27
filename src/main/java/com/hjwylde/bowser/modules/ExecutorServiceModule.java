package com.hjwylde.bowser.modules;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Provides an {@link ExecutorService} for use on background threads. If this application were larger it would be better
 * to manually manage the lifecycle of the executor service, however for a small one this executor is just shared and
 * managed by the {@link com.hjwylde.bowser.ui.frames.bowser.BowserFrame}.
 */
public final class ExecutorServiceModule {
    private static final @NotNull ExecutorService INSTANCE = Executors.newCachedThreadPool();

    public static @NotNull ExecutorService provideExecutorService() {
        return INSTANCE;
    }
}
