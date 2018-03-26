package com.hjwylde.bowser.io.schedulers;

import io.reactivex.Scheduler;

/**
 * Provides {@link Scheduler}s that are useful in the context of a Swing application.
 */
public final class SwingSchedulers {
    private SwingSchedulers() {
    }

    /**
     * Provides a {@link Scheduler} that enforces running on the Event Dispatch Thread (EDT). This is useful when
     * observing the results of long running operations, then updating the UI.
     * <p>
     * This scheduler is thread safe.
     *
     * @return a shared {@link Scheduler} meant for work on the EDT.
     * @link https://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
     */
    public static Scheduler edt() {
        return SwingEdtScheduler.getInstance();
    }
}
