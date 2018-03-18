package com.hjwylde.bowser.fileBrowsers;

import io.reactivex.Observable;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface FileBrowser {
    @NotNull Observable<? extends Path> getChildren(@NotNull Path path);

    @NotNull Observable<? extends Path> getRootDirectories();
}
