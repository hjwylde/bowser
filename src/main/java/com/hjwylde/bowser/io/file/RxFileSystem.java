package com.hjwylde.bowser.io.file;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.nio.file.FileSystem;
import java.nio.file.Path;

/**
 * An RxJava wrapper around {@link FileSystem}. The benefit of this class is to background a lot of the file operations
 * on a different {@link io.reactivex.Scheduler}. This is important as different {@link FileSystem}s may have expensive
 * IO operations.
 */
@Immutable
public final class RxFileSystem {
    private final @NotNull FileSystem fileSystem;

    private RxFileSystem(@NotNull FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    /**
     * Gets an {@link RxFileSystem} that wraps the given file system. Currently this method does not perform caching.
     * This would be an ideal feature to implement in a future version.
     *
     * @param fileSystem the file system to wrap.
     * @return a new {@link RxFileSystem}.
     * @throws NullPointerException if fileSystem is null.
     */
    public static @NotNull RxFileSystem forFileSystem(@NotNull FileSystem fileSystem) {
        return new RxFileSystem(fileSystem);
    }

    /**
     * Gets the root directories of the file system. This effectively wraps {@link FileSystem#getRootDirectories()} and
     * performs the operation in a background thread.
     *
     * @return an {@link Observable} list of the root directories.
     */
    public @NotNull Observable<Path> getRootDirectories() {
        return Observable.<Path>create(emitter -> {
            fileSystem.getRootDirectories().forEach(emitter::onNext);

            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }
}
