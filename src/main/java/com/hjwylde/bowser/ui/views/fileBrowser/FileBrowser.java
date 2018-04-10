package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.file.RxFiles;
import com.hjwylde.bowser.modules.RxFilesModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Path;
import java.util.Optional;

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
         * Gets the currently selected path. If no path is selected, then {@link Optional#empty()} is returned.
         *
         * @return the currently selected path, or {@link Optional#empty()}.
         */
        @NotNull Optional<Path> getSelectedPath();
    }

    @NotThreadSafe
    public static final class Builder {
        private Path startingPath;

        private Builder() {
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

            RxFiles rxFiles = RxFilesModule.provideRxFiles();
            FileBrowserViewModel viewModel = new FileBrowserViewModel(rxFiles);

            return new FileBrowserComponent(startingPath, viewModel);
        }

        public @NotNull Builder startingPath(@NotNull Path startingPath) {
            this.startingPath = startingPath;

            return this;
        }
    }
}
