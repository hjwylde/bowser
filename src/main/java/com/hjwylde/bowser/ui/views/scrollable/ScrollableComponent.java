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
    private final @NotNull JScrollPane scrollPane;

    /**
     * Creates a new {@link ScrollableComponent} that wraps the given {@link View}.
     *
     * @param view the view to wrap.
     */
    ScrollableComponent(@NotNull View view) {
        scrollPane = new JScrollPane(view.getComponent());
        scrollPane.setBorder(null);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return scrollPane;
    }
}
