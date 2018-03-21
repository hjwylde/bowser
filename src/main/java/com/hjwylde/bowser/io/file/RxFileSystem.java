package com.hjwylde.bowser.io.file;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Objects;

public final class RxFileSystem {
    private final @NotNull FileSystem fileSystem;

    private RxFileSystem(@NotNull FileSystem fileSystem) {
        this.fileSystem = Objects.requireNonNull(fileSystem, "fileSystem cannot be null.");
    }

    public static @NotNull RxFileSystem forFileSystem(@NotNull FileSystem fileSystem) {
        return new RxFileSystem(fileSystem);
    }

    public @NotNull Observable<Path> getRootDirectories() {
        return Observable.<Path>create(emitter -> {
            fileSystem.getRootDirectories().forEach(emitter::onNext);

            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
