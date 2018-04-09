package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.file.RxFileSystem;
import com.hjwylde.bowser.io.file.RxFiles;
import com.hjwylde.bowser.io.schedulers.SwingSchedulers;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

final class FileBrowserViewModel {
    private final @NotNull RxFiles rxFiles;
    private final @NotNull RxFileSystem rxFileSystem;

    FileBrowserViewModel(@NotNull RxFiles rxFiles, @NotNull RxFileSystem rxFileSystem) {
        this.rxFiles = rxFiles;
        this.rxFileSystem = rxFileSystem;
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
