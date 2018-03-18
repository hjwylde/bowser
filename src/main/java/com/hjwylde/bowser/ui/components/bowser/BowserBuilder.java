package com.hjwylde.bowser.ui.components.bowser;

import com.hjwylde.bowser.ui.components.View;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class BowserBuilder {
    private final @NotNull JFrame frame = new JFrame();

    private boolean built = false;

    public BowserBuilder() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public @NotNull BowserBuilder addView(@NotNull View view) {
        frame.add(view.getComponent());

        return this;
    }

    @NotNull
    public BowserView build() {
        // I'm not sure of the best way to do this. I could look at frame#isVisible, or perhaps there are other
        // methods as well. Given I don't know the corner cases here, using a variable seems the safest option.
        if (built) {
            throw new IllegalStateException("A BowserView may only be built once.");
        }

        built = true;

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.pack();
        frame.setVisible(true);

        return new BowserView(frame);
    }

    public @NotNull BowserBuilder title(@NotNull String title) {
        frame.setTitle(title);

        return this;
    }
}
