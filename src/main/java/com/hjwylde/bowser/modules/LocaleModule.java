package com.hjwylde.bowser.modules;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public final class LocaleModule {
    private LocaleModule() {
    }

    public static @NotNull Locale provideLocale() {
        // Very simple for now, but perhaps one day the user would like to customize the locale themselves
        return Locale.getDefault();
    }
}
