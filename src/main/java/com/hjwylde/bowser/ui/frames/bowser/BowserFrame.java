package com.hjwylde.bowser.ui.frames.bowser;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * An empty shell that holds onto the application {@link JFrame}. While it's unused right now, future features may need
 * it.
 */
public final class BowserFrame {
    private final @NotNull JFrame frame;

    BowserFrame(@NotNull JFrame frame) {
        this.frame = Objects.requireNonNull(frame, "frame cannot be null.");
    }
}
