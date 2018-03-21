package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.github.robtimus.filesystems.ftp.FTPEnvironment;
import com.hjwylde.bowser.io.FileSystemFactory;
import com.hjwylde.bowser.ui.dialogs.FtpConnectionDialog;
import com.hjwylde.bowser.ui.views.View;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowserBuilder;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowserView;
import com.hjwylde.bowser.ui.views.scrollable.ScrollableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Objects;
import java.util.Optional;

public final class TabbedFileBrowserView implements View {
    private final static @NotNull Logger LOGGER = LogManager.getLogger(TabbedFileBrowserView.class.getSimpleName());

    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull FileSystemFactory fileSystemFactory;

    TabbedFileBrowserView(@NotNull JTabbedPane tabbedPane, @NotNull FileSystemFactory fileSystemFactory) {
        this.tabbedPane = Objects.requireNonNull(tabbedPane, "tabbedPane cannot be null.");
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");
    }

    public void addFtpTab() {
        FtpConnectionDialog dialog = FtpConnectionDialog.builder()
                .parent(tabbedPane)
                .build();

        int result = dialog.show();
        if (result != FtpConnectionDialog.OK_OPTION) {
            return;
        }

        Optional<FileSystem> mFileSystem = getFileSystem(dialog);
        if (!mFileSystem.isPresent()) {
            return;
        }

        addTab(mFileSystem.get());
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

    private Optional<FileSystem> getFileSystem(FtpConnectionDialog dialog) {
        FTPEnvironment env = new FTPEnvironment()
                .withCredentials(dialog.getUsername(), dialog.getPassword());

        try {
            URI uri = new URI(dialog.getHost());
            FileSystem fileSystem = FileSystems.newFileSystem(uri, env);

            return Optional.of(fileSystem);
        } catch (IOException | URISyntaxException e) {
            LOGGER.warn(e);
        }

        return Optional.empty();
    }
}
