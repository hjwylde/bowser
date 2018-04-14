package com.hjwylde.bowser.util.concurrent;

import java.util.concurrent.Executor;

/**
 * Provides {@link Executor}s that are useful in the context of a Swing application.
 */
public final class SwingExecutors {
    private SwingExecutors() {
    }

    /**
     * Provides an {@link Executor} that enforces running on the Event Dispatch Thread (EDT). This is useful when
     * observing the results of long running operations, then updating the UI.
     * <p>
     * This executor is thread safe.
     *
     * @return a shared {@link Executor} meant for work on the EDT.
     * @link https://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
     */
    public static Executor edt() {
        return SwingEdtExecutor.getInstance();
    }
}
