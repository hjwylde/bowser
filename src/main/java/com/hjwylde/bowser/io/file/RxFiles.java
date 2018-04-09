package com.hjwylde.bowser.io.file;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An RxJava wrapper around the {@link Files} class helper methods. {@link Files}' operations work across different
 * {@link java.nio.file.FileSystem}s, which is beneficial to make a cross-system file browser (local, FTP, in memory).
 * The benefit of this class is to background a lot of the file operations on a different {@link Scheduler}. This is
 * important as different {@link FileSystem}s may have expensive IO operations.
 */
@Immutable
public final class RxFiles {
    /**
     * Retrieves the immediate children (files and directories) of the given parent.
     *
     * @param parent the directory to get the children of.
     * @return the parents children.
     */
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
