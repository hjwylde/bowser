package com.hjwylde.bowser.ui.views.scrollable;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Scrollable {
    private Scrollable() {
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
    }

    public static final class Builder {
        private com.hjwylde.bowser.ui.views.View view;

        private Builder() {
        }

        public @NotNull View build() {
            if (view == null) {
                throw new IllegalStateException("view cannot be null.");
            }

            return new ScrollableComponent(view);
        }

        public @NotNull Builder view(@NotNull com.hjwylde.bowser.ui.views.View view) {
            this.view = Objects.requireNonNull(view, "view cannot be null.");

            return this;
        }
    }
}
