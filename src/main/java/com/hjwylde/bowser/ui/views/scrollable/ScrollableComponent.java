package com.hjwylde.bowser.ui.views.scrollable;

import com.hjwylde.bowser.ui.views.View;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;

/**
 * A {@link View} that wraps another {@link View} in a scrollable component.
 */
@NotThreadSafe
final class ScrollableComponent implements Scrollable.View {
    private final @NotNull JComponent component;

    /**
     * Creates a new {@link ScrollableComponent} that wraps the given {@link View}.
     *
     * @param view the view to wrap.
     * @throws NullPointerException if view is null.
     */
    ScrollableComponent(@NotNull View view) {
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
