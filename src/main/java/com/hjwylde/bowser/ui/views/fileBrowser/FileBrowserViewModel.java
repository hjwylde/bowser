package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.RxFileSystem;
import com.hjwylde.bowser.io.RxFiles;
import io.reactivex.Observable;
import io.reactivex.schedulers.SwingSchedulers;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

final class FileBrowserViewModel {
    private final @NotNull RxFiles rxFiles;
    private final @NotNull RxFileSystem rxFileSystem;

    FileBrowserViewModel(@NotNull RxFiles rxFiles, @NotNull RxFileSystem rxFileSystem) {
        this.rxFiles = Objects.requireNonNull(rxFiles, "rxFiles cannot be null.");
        this.rxFileSystem = Objects.requireNonNull(rxFileSystem, "rxFileSystem cannot be null.");
    }

    public @NotNull Observable<? extends Path> getChildren(@NotNull Path parent) {
        return rxFiles.getChildren(parent)
                .filter(child -> !Files.isHidden(child))
                .sorted()
                .observeOn(SwingSchedulers.edt());
    }

    public @NotNull Observable<? extends Path> getRootDirectories() {
        return rxFileSystem.getRootDirectories()
                .sorted()
                .observeOn(SwingSchedulers.edt());
    }
}
