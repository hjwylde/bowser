package com.hjwylde.bowser.io.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Immutable
final class SwingEdtScheduler extends Scheduler {
    private static final @NotNull SwingEdtScheduler INSTANCE = new SwingEdtScheduler();

    private SwingEdtScheduler() {
    }

    static @NotNull Scheduler getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Worker createWorker() {
        return new SwingEdtWorker();
    }

    /**
     * A flag-like disposable, useful for marking actions, e.g., "stop".
     */
    @ThreadSafe
    private static class BooleanDisposable implements Disposable {
        private final @NotNull AtomicBoolean disposed = new AtomicBoolean(false);

        @Override
        public void dispose() {
            disposed.set(false);
        }

        @Override
        public boolean isDisposed() {
            return disposed.get();
        }
    }

    @ThreadSafe
    private static final class SwingEdtWorker extends Worker {
        private final @NotNull CompositeDisposable disposables = new CompositeDisposable();

        @Override
        public void dispose() {
            disposables.dispose();
        }

        @Override
        public boolean isDisposed() {
            return disposables.isDisposed();
        }

        @Override
        public Disposable schedule(Runnable runnable, long delay, TimeUnit unit) {
            long millis = Math.max(0, unit.toMillis(delay));

            Disposable flag = new BooleanDisposable();
            disposables.add(flag);

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (isDisposed() || flag.isDisposed()) {
                        return;
                    }

                    if (SwingUtilities.isEventDispatchThread()) {
                        runnable.run();
                    } else {
                        EventQueue.invokeLater(runnable);
                    }

                    flag.dispose();
                    disposables.remove(flag);
                }
            };

            Timer timer = new Timer();
            timer.schedule(task, millis);

            return new BooleanDisposable() {
                @Override
                public void dispose() {
                    super.dispose();

                    // Stop the timer, but also mark the operation (flag) as cancelled to prevent it from completing in
                    // case it gets picked up
                    timer.cancel();
                    flag.dispose();
                    disposables.remove(flag);
                }
            };
        }
    }
}

