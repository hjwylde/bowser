package com.hjwylde.bowser.ui.components.fileBrowser;

import com.hjwylde.bowser.fileBrowsers.FileBrowser;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

final class FileBrowserViewModel {
    private final @NotNull FileBrowser fileBrowser;

    FileBrowserViewModel(@NotNull FileBrowser fileBrowser) {
        this.fileBrowser = Objects.requireNonNull(fileBrowser, "fileBrowser cannot be null.");
    }

    public @NotNull Observable<? extends Path> getChildren(@NotNull Path path) {
        // TODO (hjw): Ensure we are observing on the Swing EDT.
        return fileBrowser.getChildren(path)
                .filter(child -> !Files.isHidden(child))
                .sorted();
    }

    public @NotNull Observable<? extends Path> getRootDirectories() {
        return fileBrowser.getRootDirectories()
                .sorted();
    }
}
