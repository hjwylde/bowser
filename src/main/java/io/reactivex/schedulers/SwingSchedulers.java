package io.reactivex.schedulers;

import io.reactivex.Scheduler;

public final class SwingSchedulers {
    private SwingSchedulers() {
    }

    public static Scheduler edt() {
        return SwingEdtScheduler.getInstance();
    }
}
