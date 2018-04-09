package com.hjwylde.bowser.ui.views.scrollable;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * {@link Scrollable} provides the interfaces to create and use a component that wraps another component in a scrolling
 * pane. This is useful to compose components when they can be arbitrarily large and need to fit in a small application
 * window.
 */
public final class Scrollable {
    private Scrollable() {
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
    }

    @NotThreadSafe
    public static final class Builder {
        private com.hjwylde.bowser.ui.views.View view;

        private Builder() {
        }

        /**
         * Builds and returns a new {@link Scrollable.View}. The {@link Scrollable.View} wraps the provided {@code view}
         * in a scrolling pane.This method may be called repeatedly.
         *
         * @return a new {@link Scrollable.View}.
         * @throws IllegalStateException if view is null.
         */
        public @NotNull View build() {
            if (view == null) {
                throw new IllegalStateException("view cannot be null.");
            }

            return new ScrollableComponent(view);
        }

        public @NotNull Builder view(@NotNull com.hjwylde.bowser.ui.views.View view) {
            this.view = view;

            return this;
        }
    }
}
