package com.hjwylde.bowser.modules;

import com.hjwylde.bowser.io.file.RxFiles;
import org.jetbrains.annotations.NotNull;

public final class RxFilesModule {
    private static final RxFiles RX_FILES = new RxFiles();

    private RxFilesModule() {
    }

    public static @NotNull RxFiles provideRxFiles() {
        return RX_FILES;
    }
}
