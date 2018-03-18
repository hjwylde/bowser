package com.hjwylde.bowser.fileBrowsers;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SystemFileBrowser implements FileBrowser {
    @Override
    public @NotNull Observable<Path> getChildren(@NotNull Path path) {
        return Observable.<Path>create(emitter -> {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
                ds.forEach(emitter::onNext);
            } catch (IOException e) {
                emitter.onError(e);
            }

            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public @NotNull Observable<? extends Path> getRootDirectories() {
        Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();

        return Observable.fromIterable(rootDirectories);
    }
}
