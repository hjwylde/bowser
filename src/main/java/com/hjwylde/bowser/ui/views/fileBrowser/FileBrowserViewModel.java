package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.RxFiles;
import com.hjwylde.bowser.modules.RxFilesModule;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

final class FileBrowserViewModel {
    private final @NotNull RxFiles rxFiles = RxFilesModule.provideRxFiles();

    public @NotNull Observable<? extends Path> getChildren(@NotNull Path parent) {
        // TODO (hjw): Ensure we are observing on the Swing EDT.
        return rxFiles.getChildren(parent)
                .filter(child -> !Files.isHidden(child))
                .sorted();
    }
}
