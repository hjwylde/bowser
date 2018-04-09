package com.hjwylde.bowser.io.file.archives;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * An {@link ArchiveFileFactory} that supports creating ZIP {@link ArchiveFile}s ("application/zip" files).
 */
@AutoService(ArchiveFileFactory.class)
@Immutable
@SuppressWarnings("unused")
public final class ZipArchiveFileFactory implements ArchiveFileFactory<ZipArchiveFile> {
    private static final @NotNull List<String> SUPPORTED_CONTENT_TYPES = Collections.singletonList("application/zip");

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ZipArchiveFile createArchiveFile(@NotNull Path path) {
        return new ZipArchiveFile(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return SUPPORTED_CONTENT_TYPES.contains(contentType);
    }
}
