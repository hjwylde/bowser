package com.hjwylde.bowser.ui.components.bowser;

import com.hjwylde.bowser.io.FileSystemFactory;
import com.hjwylde.bowser.ui.components.View;
import com.hjwylde.bowser.ui.components.fileBrowser.FileBrowserBuilder;
import com.hjwylde.bowser.ui.components.fileBrowser.FileBrowserView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystem;
import java.util.Objects;

public final class BowserView implements View {
    private final @NotNull JFrame frame;
    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull FileSystemFactory fileSystemFactory;

    BowserView(@NotNull JFrame frame, @NotNull JTabbedPane tabbedPane, @NotNull FileSystemFactory fileSystemFactory) {
        this.frame = Objects.requireNonNull(frame, "frame cannot be null.");
        this.tabbedPane = Objects.requireNonNull(tabbedPane, "tabbedPane cannot be null.");
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");
    }

    public void addDefaultFileBrowserTab() {
        addFileBrowserTab(fileSystemFactory.getFileSystem());
    }

    @Override
    public @NotNull Component getComponent() {
        return frame;
    }

    private void addFileBrowserTab(@NotNull FileSystem fileSystem) {
        FileBrowserView fileBrowserView = new FileBrowserBuilder()
                .fileSystem(fileSystem)
                .build();

        // TODO (hjw): Dynamically set the tab name
        tabbedPane.addTab("Tab", fileBrowserView.getComponent());
    }
}
