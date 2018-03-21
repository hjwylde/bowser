package com.hjwylde.bowser.ui.views.scrollable;

import com.hjwylde.bowser.ui.views.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class ScrollableView implements View {
    private final @NotNull JComponent component;

    public ScrollableView(@NotNull View view) {
        component = new JScrollPane(view.getComponent());
    }

    public @NotNull JComponent getComponent() {
        return component;
    }
}
