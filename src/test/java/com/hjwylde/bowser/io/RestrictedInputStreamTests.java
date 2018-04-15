package com.hjwylde.bowser.io;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestrictedInputStreamTests {
    private static @NotNull InputStream createInputStream(int length) {
        return new ByteArrayInputStream(new byte[length]);
    }

    private static @NotNull RestrictedInputStream createRestrictedInputStream(int length, int limit) {
        InputStream in = createInputStream(length);

        return new RestrictedInputStream(in, limit);
    }

    @Nested
    class Read {
        @Test
        void doesNothingWhenLimitGreaterThanAvailable() throws IOException {
            RestrictedInputStream rin = createRestrictedInputStream(10, 100);

            assertEquals(10, rin.read(new byte[10]));
        }

        @Test
        void doesNothingWhenLimitMatchesAvailable() throws IOException {
            RestrictedInputStream rin = createRestrictedInputStream(0, 0);

            assertEquals(-1, rin.read());
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        void throwsIOExceptionWhenOverLimit() {
            RestrictedInputStream rin = createRestrictedInputStream(100, 0);

            assertThrows(IOException.class, rin::read);
            assertThrows(IOException.class, () -> rin.read(new byte[10]));
            assertThrows(IOException.class, () -> rin.read(new byte[10], 0, 10));
        }
    }

    @Nested
    class Reset {
        @Test
        void resetsCount() throws IOException {
            RestrictedInputStream rin = createRestrictedInputStream(10, 10);

            assertEquals(5, rin.read(new byte[5], 0, 5));
            rin.mark(0);
            assertEquals(5, rin.read(new byte[5], 0, 5));
            rin.reset();
            assertEquals(5, rin.read(new byte[5], 0, 5));
        }
    }

    @Nested
    class Skip {
        @Test
        void doesNothingWhenLessThanLimit() throws IOException {
            RestrictedInputStream rin = createRestrictedInputStream(10, 10);

            assertEquals(5, rin.skip(5));
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        @Test
        void throwsIOExceptionWhenOverLimit() {
            RestrictedInputStream rin = createRestrictedInputStream(10, 0);

            assertThrows(IOException.class, () -> rin.skip(20));
        }
    }
}
