package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowser;
import com.hjwylde.bowser.ui.views.scrollable.Scrollable;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.function.BiFunction;

@NotThreadSafe
final class TabbedFileBrowserComponent implements TabbedFileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(TabbedFileBrowserComponent.class.getSimpleName());

    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull TabbedFileBrowserViewModel viewModel;

    private final @NotNull FileSystemFactory fileSystemFactory;

    TabbedFileBrowserComponent(@NotNull JTabbedPane tabbedPane, @NotNull FileSystemFactory fileSystemFactory, @NotNull TabbedFileBrowserViewModel viewModel) {
        this.tabbedPane = tabbedPane;
        this.fileSystemFactory = fileSystemFactory;
        this.viewModel = viewModel;
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
        OnSelectStartingPathObserver handler = new OnSelectStartingPathObserver();

        viewModel.selectStartingPath(fileSystem)
                .handleAsync(handler, SwingExecutors.edt());
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

    @NotThreadSafe
    private final class OnSelectStartingPathObserver implements BiFunction<Path, Throwable, Void> {
        @Override
        public Void apply(Path path, Throwable throwable) {
            if (path != null) {
                onSuccess(path);
            } else if (throwable != null) {
                onError(throwable);
            }

            return null;
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            handleError(throwable);
        }

        private void onSuccess(@NotNull Path path) {
            FileBrowser.View fileBrowserView = FileBrowser.builder()
                    .startingPath(path)
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
    }
}
