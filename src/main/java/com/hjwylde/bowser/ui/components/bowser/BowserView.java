package com.hjwylde.bowser.ui.components.bowser;

import com.hjwylde.bowser.ui.components.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class BowserView implements View {
    private final @NotNull JFrame frame;

    BowserView(@NotNull JFrame frame) {
        this.frame = Objects.requireNonNull(frame, "frame cannot be null.");
    }

    @Override
    public @NotNull Component getComponent() {
        return frame;
    }
}
