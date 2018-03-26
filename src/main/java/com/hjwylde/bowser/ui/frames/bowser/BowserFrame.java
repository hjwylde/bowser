package com.hjwylde.bowser.ui.frames.bowser;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

/**
 * An empty shell that holds onto the application {@link JFrame}. While it's unused right now, future features may need
 * it.
 */
public final class BowserFrame {
    BowserFrame(@NotNull JFrame frame) {
        // Frame is unused right now
        Objects.requireNonNull(frame, "frame cannot be null.");
    }
}
