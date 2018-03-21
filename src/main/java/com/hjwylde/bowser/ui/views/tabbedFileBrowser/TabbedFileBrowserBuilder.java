package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public final class TabbedFileBrowserBuilder {
    private final @NotNull JTabbedPane tabbedPane = new JTabbedPane();

    private FileSystemFactory fileSystemFactory;

    public TabbedFileBrowserBuilder() {
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public @NotNull TabbedFileBrowserView build() {
        if (fileSystemFactory == null) {
            throw new IllegalStateException("fileSystemFactory cannot be null.");
        }

        return new TabbedFileBrowserView(tabbedPane, fileSystemFactory);
    }

    public @NotNull TabbedFileBrowserBuilder fileSystemFactory(@NotNull FileSystemFactory fileSystemFactory) {
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");

        return this;
    }
}
