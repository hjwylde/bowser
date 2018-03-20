package com.hjwylde.bowser.ui.components.scrollable;

import com.hjwylde.bowser.ui.components.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public final class ScrollableView implements View {
    private final @NotNull Component component;

    public ScrollableView(@NotNull View view) {
        component = new JScrollPane(view.getComponent());
    }

    public @NotNull Component getComponent() {
        return component;
    }
}
