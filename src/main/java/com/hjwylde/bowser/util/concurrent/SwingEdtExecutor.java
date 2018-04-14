package com.hjwylde.bowser.util.concurrent;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executor;

@Immutable
final class SwingEdtExecutor implements Executor {
    private static final @NotNull SwingEdtExecutor INSTANCE = new SwingEdtExecutor();

    private SwingEdtExecutor() {
    }

    static @NotNull Executor getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(@NotNull Runnable command) {
        if (SwingUtilities.isEventDispatchThread()) {
            command.run();
        } else {
            EventQueue.invokeLater(command);
        }
    }
}
