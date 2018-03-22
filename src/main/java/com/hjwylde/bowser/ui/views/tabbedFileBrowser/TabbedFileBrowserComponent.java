package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.github.robtimus.filesystems.ftp.FTPEnvironment;
import com.hjwylde.bowser.io.file.FileSystemFactory;
import com.hjwylde.bowser.ui.dialogs.FtpConnectionDialog;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowserBuilder;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowserView;
import com.hjwylde.bowser.ui.views.scrollable.Scrollable;
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

final class TabbedFileBrowserComponent implements TabbedFileBrowser.View {
    private final static @NotNull Logger LOGGER = LogManager.getLogger(TabbedFileBrowserComponent.class.getSimpleName());

    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull FileSystemFactory fileSystemFactory;

    TabbedFileBrowserComponent(@NotNull JTabbedPane tabbedPane, @NotNull FileSystemFactory fileSystemFactory) {
        this.tabbedPane = Objects.requireNonNull(tabbedPane, "tabbedPane cannot be null.");
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");
    }

    @Override
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

    @Override
    public void addTab() {
        addTab(fileSystemFactory.getFileSystem());
    }

    @Override
    public @NotNull JComponent getComponent() {
        return tabbedPane;
    }

    @Override
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

        Scrollable.View scrollableView = Scrollable.builder()
                .view(fileBrowserView)
                .build();
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
            LOGGER.warn(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
