package com.hjwylde.bowser.ui.components.bowser;

import com.hjwylde.bowser.io.FileSystemFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public final class BowserBuilder {
    private final @NotNull JFrame frame = new JFrame();
    private final @NotNull JTabbedPane tabbedPane = new JTabbedPane();

    private FileSystemFactory fileSystemFactory;

    private boolean built = false;

    public BowserBuilder() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public @NotNull BowserView build() {
        // I'm not sure of the best way to do this. I could look at frame#isVisible, or perhaps there are other
        // methods as well. Given I don't know the corner cases here, using a variable seems the safest option.
        if (built) {
            throw new IllegalStateException("A BowserView may only be built once.");
        }

        built = true;

        frame.pack();
        frame.setVisible(true);

        return new BowserView(frame, tabbedPane, fileSystemFactory);
    }

    public @NotNull BowserBuilder fileSystemFactory(@NotNull FileSystemFactory fileSystemFactory) {
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");

        return this;
    }

    public @NotNull BowserBuilder title(@NotNull String title) {
        frame.setTitle(title);

        return this;
    }
}
