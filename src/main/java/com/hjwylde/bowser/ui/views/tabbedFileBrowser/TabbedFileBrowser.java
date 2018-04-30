package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.io.file.FileSystemFactory;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * {@link TabbedFileBrowser} provides the interfaces to create and use a component that adds/removes different file
 * browsing tabs. Multiple tabs can/will be shown at once, and each may have their own unique behaviour depending on
 * which {@link FileSystem} they have backing them.
 */
public final class TabbedFileBrowser {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(TabbedFileBrowser.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_BUTTON_BACK = "buttonBack";
    private static final @NotNull String RESOURCE_BUTTON_FORWARD = "buttonForward";

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
         * Adds a browser tab using the given starting path. The file system is taken from the path.
         */
        void addTab(@NotNull Path path);

        /**
         * Adds a browser tab using the given file system.
         */
        void addTab(@NotNull FileSystem fileSystem);

        /**
         * Adds a browser tab using the default file system factory.
         */
        void addTab();

        /**
         * Adds a tab change listener. The listener will be informed of the selected file browser whenever the current
         * tab changes. When a listener is first added, it is immediately informed of the currently selected file
         * browser.
         *
         * @param listener the listener.
         */
        void addTabChangeListener(Consumer<FileBrowser.View> listener);

        /**
         * Gets the current tab, or {@link Optional#empty()} if there are no tabs.
         *
         * @return the current tab, or {@link Optional#empty()}.
         */
        @NotNull Optional<FileBrowser.View> getCurrentTab();

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

            JButton navigateBackButton = new JButton(RESOURCES.getString(RESOURCE_BUTTON_BACK));
            JButton navigateForwardButton = new JButton(RESOURCES.getString(RESOURCE_BUTTON_FORWARD));

            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            buttonsPanel.add(navigateBackButton);
            buttonsPanel.add(navigateForwardButton);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(buttonsPanel, BorderLayout.PAGE_START);
            panel.add(tabbedPane);

            TabbedFileBrowserViewModel viewModel = new TabbedFileBrowserViewModel();

            return new TabbedFileBrowserComponent(
                    panel, navigateBackButton, navigateForwardButton, tabbedPane, fileSystemFactory, viewModel
            );
        }

        public @NotNull Builder fileSystemFactory(@NotNull FileSystemFactory fileSystemFactory) {
            this.fileSystemFactory = fileSystemFactory;

            return this;
        }
    }
}
