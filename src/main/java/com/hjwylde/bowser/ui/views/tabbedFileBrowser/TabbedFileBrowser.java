package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

public final class TabbedFileBrowser {
    private TabbedFileBrowser() {
    }

    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
        void addFtpTab();

        void addTab();

        void removeCurrentTab();
    }

    public static final class Builder {
        private final @NotNull JTabbedPane tabbedPane = new JTabbedPane();

        private FileSystemFactory fileSystemFactory;

        private Builder() {
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }

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
