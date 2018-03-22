package com.hjwylde.bowser.modules;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * A module for providing a {@link Locale} instance. The application locale may change between versions.
 */
public final class LocaleModule {
    private LocaleModule() {
    }

    /**
     * Provides an application default {@link Locale}.
     *
     * @return the application default {@link Locale}.
     */
    public static @NotNull Locale provideLocale() {
        return Locale.getDefault();
    }
}
