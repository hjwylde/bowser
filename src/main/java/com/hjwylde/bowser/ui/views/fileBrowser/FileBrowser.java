package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.views.fileDirectory.FileDirectory;
import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * {@link FileBrowser} provides the interfaces to create and use a component that displays a file tree. The file tree is
 * navigatable and interactable. E.g., it is possible to extract and open different file types.
 */
public final class FileBrowser {
    private FileBrowser() {
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
         * Navigates back a directory. This has no effect if the file browser is currently pointing to the root.
         */
        void navigateBack();

        /**
         * Navigates forward a directory. This has no effect if the file browser is currently pointing at the last
         * state.
         */
        void navigateForward();
    }

    @NotThreadSafe
    public static final class Builder {
        private Path startingPath;

        private Builder() {
        }

        private static @NotNull JComponent wrapInsideScrollPane(@NotNull JComponent component) {
            JScrollPane scrollableComponent = new JScrollPane(component);
            scrollableComponent.setBorder(null);
            scrollableComponent.getHorizontalScrollBar().setUnitIncrement(16);
            scrollableComponent.getVerticalScrollBar().setUnitIncrement(16);

            return scrollableComponent;
        }

        /**
         * Builds and returns a new {@link FileBrowser.View}. The file browser view must have a starting path to
         * reference.
         *
         * @return a new {@link FileBrowser.View}.
         * @throws IllegalStateException if startingPath is null.
         */
        public @NotNull View build() {
            if (startingPath == null) {
                throw new IllegalStateException("startingPath must be set.");
            }

            FileDirectory.View fileDirectoryView = FileDirectory.builder()
                    .startingPath(startingPath)
                    .build();
            JComponent scrollableFileDirectory = wrapInsideScrollPane(fileDirectoryView.getComponent());

            FilePreview.View filePreviewView = FilePreview.builder().build();
            JComponent scrollableFilePreview = wrapInsideScrollPane(filePreviewView.getComponent());

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollableFileDirectory, scrollableFilePreview);
            splitPane.setDividerLocation(0.5);
            splitPane.setResizeWeight(0.5);

            return new FileBrowserComponent(splitPane, fileDirectoryView, filePreviewView);
        }

        public @NotNull Builder startingPath(@NotNull Path startingPath) {
            this.startingPath = startingPath;

            return this;
        }
    }
}
