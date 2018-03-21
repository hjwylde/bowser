package com.hjwylde.bowser.io.file.archives;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public interface ArchiveFile {
    void extract(@NotNull Path destination) throws IOException;
}
