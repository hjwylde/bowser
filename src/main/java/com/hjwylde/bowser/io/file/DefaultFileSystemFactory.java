package com.hjwylde.bowser.io.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

public final class DefaultFileSystemFactory implements FileSystemFactory {
    private static final @NotNull FileSystemFactory INSTANCE = new DefaultFileSystemFactory();

    private DefaultFileSystemFactory() {
    }

    public static @NotNull FileSystemFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public @NotNull FileSystem getFileSystem() {
        return FileSystems.getDefault();
    }
}
