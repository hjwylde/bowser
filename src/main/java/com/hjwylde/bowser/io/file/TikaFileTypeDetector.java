package com.hjwylde.bowser.io.file;

import org.apache.tika.Tika;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;

/**
 * A {@link FileTypeDetector} that uses {@link Tika} to help bolster the native detectors. For some operating systems
 * the fallback file detector does nothing. Including this detector helps to ensure that there will be at least one
 * available.
 */
@Immutable
public class TikaFileTypeDetector extends FileTypeDetector {
    private final @NotNull Tika tika = new Tika();

    /**
     * {@inheritDoc}
     */
    @Override
    public String probeContentType(Path path) throws IOException {
        return tika.detect(path);
    }
}
