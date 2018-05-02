package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowser;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NotThreadSafe
final class TabbedFileBrowserComponent implements TabbedFileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(TabbedFileBrowserComponent.class.getSimpleName());

    private final @NotNull JComponent root;
    private final @NotNull JTabbedPane tabbedPane;

    private final @NotNull List<FileBrowser.View> fileBrowserViews = new ArrayList<>();

    private final @NotNull List<Consumer<Optional<FileBrowser.View>>> tabChangeListeners = new ArrayList<>();

    private final @NotNull TabbedFileBrowserViewModel viewModel;

    private final @NotNull FileSystemFactory fileSystemFactory;

    // TODO (hjw): Find a better way to deal with the navigation buttons. Perhaps these need to be a separate component
    TabbedFileBrowserComponent(@NotNull JComponent root, @NotNull JButton navigateBackButton,
                               @NotNull JButton navigateForwardButton, @NotNull JTabbedPane tabbedPane,
                               @NotNull FileSystemFactory fileSystemFactory,
                               @NotNull TabbedFileBrowserViewModel viewModel) {
        this.root = root;
        this.tabbedPane = tabbedPane;
        this.fileSystemFactory = fileSystemFactory;
        this.viewModel = viewModel;

        initialiseNavigationButtons(navigateBackButton, navigateForwardButton);
        initialiseTabChangeListener();
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
    public void addTab(@NotNull Path path) {
        FileBrowser.View fileBrowserView = FileBrowser.builder()
                .startingPath(path)
                .build();

        fileBrowserViews.add(fileBrowserView);

        JComponent component = fileBrowserView.getComponent();

        tabbedPane.addTab("", component);
        tabbedPane.setSelectedComponent(component);

        fileBrowserView.addDirectoryChangeListener(directory -> {
            int index = tabbedPane.indexOfComponent(component);
            if (index != -1) {
                StringBuilder sb = new StringBuilder();

                if (!directory.getFileSystem().equals(FileSystems.getDefault())) {
                    URI uri = directory.getRoot().toUri();

                    String fileSystemName;
                    try {
                        fileSystemName = URLDecoder.decode(uri.toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        fileSystemName = uri.toString();
                    }

                    sb.append(fileSystemName);
                    sb.append(" - ");
                }

                sb.append(fileBrowserView.getTitle());

                tabbedPane.setTitleAt(index, sb.toString());
            }

            notifyTabChangeListeners();
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTab(@NotNull FileSystem fileSystem) {
        OnSelectStartingPathConsumer handler = new OnSelectStartingPathConsumer();

        viewModel.selectStartingPath(fileSystem)
                .whenCompleteAsync(handler, SwingExecutors.edt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTabChangeListener(Consumer<Optional<FileBrowser.View>> listener) {
        tabChangeListeners.add(listener);

        Optional<FileBrowser.View> mFileBrowserView = getCurrentTab();
        listener.accept(mFileBrowserView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<FileBrowser.View> getCurrentTab() {
        int index = tabbedPane.getSelectedIndex();
        if (index < 0) {
            return Optional.empty();
        }

        return Optional.of(fileBrowserViews.get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCurrentTab() {
        int index = tabbedPane.getSelectedIndex();
        if (index >= 0) {
            fileBrowserViews.remove(index);
            tabbedPane.remove(index);

            notifyTabChangeListeners();
        }
    }

    private void initialiseNavigationButtons(@NotNull JButton navigateBackButton, @NotNull JButton navigateForwardButton) {
        navigateBackButton.addActionListener(e -> {
            Optional<FileBrowser.View> mFileBrowserView = getCurrentTab();

            mFileBrowserView.ifPresent(FileBrowser.View::navigateBack);
        });

        navigateForwardButton.addActionListener(e -> {
            Optional<FileBrowser.View> mFileBrowserView = getCurrentTab();

            mFileBrowserView.ifPresent(FileBrowser.View::navigateForward);
        });
    }

    private void initialiseTabChangeListener() {
        tabbedPane.addChangeListener(e -> notifyTabChangeListeners());
    }

    private void notifyTabChangeListeners() {
        Optional<FileBrowser.View> mFileBrowserView = getCurrentTab();

        tabChangeListeners.forEach(listener -> listener.accept(mFileBrowserView));
    }

    @NotThreadSafe
    private final class OnSelectStartingPathConsumer implements BiConsumer<Path, Throwable> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void accept(Path path, Throwable throwable) {
            if (path != null) {
                onSuccess(path);
            } else if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            // throwable is a CompletionException, let's handle the actual cause
            handleError(throwable.getCause());
        }

        private void onSuccess(@NotNull Path path) {
            addTab(path);
        }
    }
}
