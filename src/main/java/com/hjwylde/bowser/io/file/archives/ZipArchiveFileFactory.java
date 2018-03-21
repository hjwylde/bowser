package com.hjwylde.bowser.io.file.archives;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@AutoService(ArchiveFileFactory.class)
public final class ZipArchiveFileFactory implements ArchiveFileFactory<ZipArchiveFile> {
    private static final List<String> SUPPORTED_CONTENT_TYPES = Collections.singletonList("application/zip");

    @Override
    public @NotNull ZipArchiveFile getArchiveFile(@NotNull Path path) {
        return new ZipArchiveFile(path);
    }

    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return SUPPORTED_CONTENT_TYPES.contains(contentType);
    }
}
