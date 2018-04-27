package com.hjwylde.bowser.ui.views.fileDirectory;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * {@link FileDirectory} provides the interfaces to create and use a component that displays a file tree. The file tree is
 * navigatable and interactable. E.g., it is possible to extract and open different file types.
 */
public final class FileDirectory {
    private FileDirectory() {
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
        /**
         * Adds a directory change listener. The listener will be informed of the new directory path whenever the file
         * browser changes the current directory listing. When a listener is first added, it is immediately informed of
         * the current directory path.
         *
         * @param listener the listener.
         */
        void addDirectoryChangeListener(@NotNull Consumer<Path> listener);

        /**
         * Adds a selected path change listener. The listener will be informed of user selection changes. When a
         * listener is first added, it is immediately informed of the current selected path.
         * If the path is deselected, {@link Optional#empty()} is passed to the listener.
         *
         * @param listener the listener.
         */
        void addSelectedPathChangeListener(@NotNull Consumer<Optional<Path>> listener);

        /**
         * Gets the current directory.
         *
         * @return the current directory.
         */
        @NotNull Path getDirectory();

        /**
         * Gets the currently selected path. If no path is selected, then {@link Optional#empty()} is returned.
         *
         * @return the currently selected path, or {@link Optional#empty()}.
         */
        @NotNull Optional<Path> getSelectedPath();

        /**
         * Gets a title for this file directory.
         *
         * @return the title.
         */
        @NotNull String getTitle();

        /**
         * Sets the current directory to the provided path. The view is to deal with asynchronously finding the
         * directory's children and refreshing the listing.
         *
         * @param directory the new directory for listing.
         */
        void setDirectory(@NotNull Path directory);
    }

    @NotThreadSafe
    public static final class Builder {
        private Path startingPath;

        private Builder() {
        }

        /**
         * Builds and returns a new {@link FileDirectory.View}. The file directory view must have a starting path to
         * reference.
         *
         * @return a new {@link FileDirectory.View}.
         * @throws IllegalStateException if startingPath is null.
         */
        public @NotNull View build() {
            if (startingPath == null) {
                throw new IllegalStateException("startingPath must be set.");
            }

            DefaultListModel<FileNode> listModel = new DefaultListModel<>();
            JList<FileNode> list = new JList<>(listModel);

            FileDirectoryViewModel viewModel = new FileDirectoryViewModel();

            return new FileDirectoryComponent(list, listModel, startingPath, viewModel);
        }

        public @NotNull Builder startingPath(@NotNull Path startingPath) {
            this.startingPath = startingPath;

            return this;
        }
    }
}
