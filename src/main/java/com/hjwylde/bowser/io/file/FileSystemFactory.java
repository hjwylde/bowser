package com.hjwylde.bowser.io.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;

public interface FileSystemFactory {
    @NotNull FileSystem getFileSystem();
}
