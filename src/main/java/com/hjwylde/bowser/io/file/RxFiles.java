package com.hjwylde.bowser.io.file;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class RxFiles {
    public @NotNull Observable<Path> getChildren(@NotNull Path parent) {
        return Observable.<Path>create(emitter -> {
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(parent)) {
                ds.forEach(emitter::onNext);
            } catch (IOException e) {
                emitter.onError(e);
            }

            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
