package com.hjwylde.bowser.io.file.archives;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public interface ArchiveFileFactory<E extends ArchiveFile> {
    @NotNull E getArchiveFile(@NotNull Path path);

    boolean isSupportedContentType(@NotNull String contentType);
}
