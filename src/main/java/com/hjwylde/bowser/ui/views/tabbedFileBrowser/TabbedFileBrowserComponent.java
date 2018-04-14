package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowser;
import com.hjwylde.bowser.ui.views.scrollable.Scrollable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

@NotThreadSafe
final class TabbedFileBrowserComponent implements TabbedFileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(TabbedFileBrowserComponent.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(TabbedFileBrowserComponent.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_NO_STARTING_PATH = "errorNoStartingPath";

    private static final @NotNull String USER_HOME = System.getProperty("user.home");

    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull FileSystemFactory fileSystemFactory;

    TabbedFileBrowserComponent(@NotNull JTabbedPane tabbedPane, @NotNull FileSystemFactory fileSystemFactory) {
        this.tabbedPane = tabbedPane;
        this.fileSystemFactory = fileSystemFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTab() {
        addTab(fileSystemFactory.getFileSystem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTab(@NotNull FileSystem fileSystem) {
        Optional<Path> mStartingPath = selectStartingPath(fileSystem);
        if (!mStartingPath.isPresent()) {
            Exception e = new Exception(RESOURCES.getString(RESOURCE_ERROR_NO_STARTING_PATH));
            LOGGER.warn(e.getMessage(), e);

            handleError(e);

            return;
        }

        Path startingPath = mStartingPath.get();

        FileBrowser.View fileBrowserView = FileBrowser.builder()
                .startingPath(startingPath)
                .build();

        Scrollable.View scrollableView = Scrollable.builder()
                .view(fileBrowserView)
                .build();

        JComponent component = scrollableView.getComponent();

        tabbedPane.addTab("", component);
        tabbedPane.setSelectedComponent(component);

        fileBrowserView.addDirectoryChangeListener(directory -> {
            int index = tabbedPane.indexOfComponent(component);
            if (index != -1) {
                Path fileName = directory.getFileName() != null ? directory.getFileName() : directory;

                tabbedPane.setTitleAt(index, fileName.toString());
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return tabbedPane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCurrentTab() {
        Component component = tabbedPane.getSelectedComponent();
        if (component != null) {
            tabbedPane.remove(component);
        }
    }

    private @NotNull Optional<Path> selectStartingPath(@NotNull FileSystem fileSystem) {
        // TODO (hjw): Background this method
        // Default to the user home directory first
        Path path = fileSystem.getPath(USER_HOME);
        if (Files.exists(path)) {
            return Optional.of(path);
        }

        Iterator<Path> it = fileSystem.getRootDirectories().iterator();
        if (!it.hasNext()) {
            return Optional.empty();
        }

        // Else, display the first root directory available
        // It's unlikely that there will be more than one root directory available. Windows is the only common situation
        // that this occurs in, and for that scenario we'd expect the USER_HOME path to exist.
        return Optional.of(it.next());
    }
}
