package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.file.FileSystem;
import java.util.Objects;

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
         * Connects to a new FTP file system and adds a browser tab. It is up to individual subclasses to determine how
         * and where the FTP connection details are gathered.
         */
        void addFtpTab();

        /**
         * Adds a browser tab using the default file system factory.
         */
        void addTab();

        /**
         * Removes the current tab.
         */
        void removeCurrentTab();
    }

    public static final class Builder {
        private final @NotNull JTabbedPane tabbedPane = new JTabbedPane();

        private FileSystemFactory fileSystemFactory;

        private Builder() {
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
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

            return new TabbedFileBrowserComponent(tabbedPane, fileSystemFactory);
        }

        public @NotNull Builder fileSystemFactory(@NotNull FileSystemFactory fileSystemFactory) {
            this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");

            return this;
        }
    }
}
