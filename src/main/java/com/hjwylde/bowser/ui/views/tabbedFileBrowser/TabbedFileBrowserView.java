package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.FileSystemFactory;
import com.hjwylde.bowser.ui.views.View;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowserBuilder;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowserView;
import com.hjwylde.bowser.ui.views.scrollable.ScrollableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystem;
import java.util.Objects;

public final class TabbedFileBrowserView implements View {
    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull FileSystemFactory fileSystemFactory;

    TabbedFileBrowserView(@NotNull JTabbedPane tabbedPane, @NotNull FileSystemFactory fileSystemFactory) {
        this.tabbedPane = Objects.requireNonNull(tabbedPane, "tabbedPane cannot be null.");
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");
    }

    public void addTab() {
        addTab(fileSystemFactory.getFileSystem());
    }

    @Override
    public @NotNull JComponent getComponent() {
        return tabbedPane;
    }

    public void removeCurrentTab() {
        Component component = tabbedPane.getSelectedComponent();
        if (component != null) {
            tabbedPane.remove(component);
        }
    }

    private void addTab(@NotNull FileSystem fileSystem) {
        FileBrowserView fileBrowserView = new FileBrowserBuilder()
                .fileSystem(fileSystem)
                .build();

        ScrollableView scrollableView = new ScrollableView(fileBrowserView);
        JComponent component = scrollableView.getComponent();

        // TODO (hjw): Dynamically set the tab name
        tabbedPane.addTab("Tab", component);
        tabbedPane.setSelectedComponent(component);
    }
}
