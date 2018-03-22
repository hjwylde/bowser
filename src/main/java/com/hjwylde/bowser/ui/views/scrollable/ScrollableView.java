package com.hjwylde.bowser.ui.views.scrollable;

import com.hjwylde.bowser.ui.views.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * A {@link View} that wraps another {@link View} in a scrollable component.
 */
public final class ScrollableView implements View {
    private final @NotNull JComponent component;

    /**
     * Creates a new {@link ScrollableView} that wraps the given {@link View}.
     *
     * @param view the view to wrap.
     * @throws NullPointerException if view is null.
     */
    public ScrollableView(@NotNull View view) {
        component = new JScrollPane(view.getComponent());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return component;
    }
}
