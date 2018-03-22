package com.hjwylde.bowser.modules;

import com.hjwylde.bowser.io.file.RxFiles;
import org.jetbrains.annotations.NotNull;

/**
 * A module for providing an {@link RxFiles} instance. {@link RxFiles} is a final class right now, but having a single
 * location for retrieving a default {@link RxFiles} is useful to ease refactoring in the future.
 */
public final class RxFilesModule {
    private static final RxFiles RX_FILES = new RxFiles();

    private RxFilesModule() {
    }

    /**
     * Gets the singleton {@link RxFiles} instance.
     *
     * @return the singleton instance.
     */
    public static @NotNull RxFiles provideRxFiles() {
        return RX_FILES;
    }
}
