package com.hjwylde.bowser.modules;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocaleModuleTests {
    @Nested
    class StaticMethods {
        @Nested
        class ProvideLocale {
            @Test
            void returnsDefaultLocale() {
                Locale locale = LocaleModule.provideLocale();

                assertEquals(Locale.getDefault(), locale);
            }
        }
    }
}
