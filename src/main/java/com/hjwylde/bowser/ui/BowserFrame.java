package com.hjwylde.bowser.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public final class BowserFrame {
    private final @NotNull JFrame frame;

    BowserFrame(@NotNull JFrame frame) {
        this.frame = Objects.requireNonNull(frame, "frame cannot be null.");
    }
}
