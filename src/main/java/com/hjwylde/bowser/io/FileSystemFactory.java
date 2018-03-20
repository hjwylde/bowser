package com.hjwylde.bowser.io;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;

public interface FileSystemFactory {
    @NotNull FileSystem getFileSystem();
}
