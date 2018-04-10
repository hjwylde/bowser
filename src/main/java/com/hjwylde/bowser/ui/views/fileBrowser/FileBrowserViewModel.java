package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.file.RxFiles;
import com.hjwylde.bowser.io.schedulers.SwingSchedulers;
import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Files;
import java.nio.file.Path;

@Immutable
final class FileBrowserViewModel {
    private final @NotNull RxFiles rxFiles;

    FileBrowserViewModel(@NotNull RxFiles rxFiles) {
        this.rxFiles = rxFiles;
    }

    public @NotNull Observable<? extends Path> getChildren(@NotNull Path parent) {
        return rxFiles.getChildren(parent)
                .filter(child -> !Files.isHidden(child))
                .sorted()
                .observeOn(SwingSchedulers.edt());
    }
}
