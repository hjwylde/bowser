package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.FileSystem;

/**
 * {@link TabbedFileBrowser} provides the interfaces to create and use a component that adds/removes different file
 * browsing tabs. Multiple tabs can/will be shown at once, and each may have their own unique behaviour depending on
 * which {@link FileSystem} they have backing them.
 */
public final class TabbedFileBrowser {
    private TabbedFileBrowser() {
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
         * Adds a browser tab using the given file system.
         */
        void addTab(@NotNull FileSystem fileSystem);

        /**
         * Adds a browser tab using the default file system factory.
         */
        void addTab();

        /**
         * Removes the current tab.
         */
        void removeCurrentTab();
    }

    @NotThreadSafe
    public static final class Builder {
        private FileSystemFactory fileSystemFactory;

        private Builder() {
        }

        /**
         * Builds and returns a new {@link TabbedFileBrowser.View}. The {@link TabbedFileBrowser.View} references the
         * given file system factory. The view will have no tabs to begin with. If you wish for it to start with a
         * default tab, call {@link View#addTab()} after this method.
         *
         * @return a new {@link TabbedFileBrowser.View}.
         * @throws IllegalStateException if fileSystemFactory is null.
         */
        public @NotNull View build() {
            if (fileSystemFactory == null) {
                throw new IllegalStateException("fileSystemFactory cannot be null.");
            }

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

            TabbedFileBrowserViewModel viewModel = new TabbedFileBrowserViewModel();

            return new TabbedFileBrowserComponent(tabbedPane, fileSystemFactory, viewModel);
        }

        public @NotNull Builder fileSystemFactory(@NotNull FileSystemFactory fileSystemFactory) {
            this.fileSystemFactory = fileSystemFactory;

            return this;
        }
    }
}
