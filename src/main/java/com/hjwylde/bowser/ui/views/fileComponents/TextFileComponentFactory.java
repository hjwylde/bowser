package com.hjwylde.bowser.ui.views.fileComponents;

import com.hjwylde.bowser.io.RestrictedInputStream;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.*;
import java.util.stream.Collectors;

/**
 * A {@link FileComponentFactory} that supports creating text {@link FileComponent}s ("text/plain" files).
 */
@Immutable
@SuppressWarnings("unused")
public final class TextFileComponentFactory implements FileComponentFactory<TextFileComponent> {
    private static final int MAX_BYTES = 1024 * 1024 * 1024; // 1 GB

    private static final String SUPPORTED_CONTENT_TYPE = "text/";

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TextFileComponent createFileComponent(@NotNull InputStream in) throws IOException {
        RestrictedInputStream lin = new RestrictedInputStream(in, MAX_BYTES);
        InputStreamReader inr = new InputStreamReader(lin);
        BufferedReader br = new BufferedReader(inr);

        try {
            String text = br.lines()
                    .collect(Collectors.joining("\n"));

            return createFileComponent(text);
        } catch (UncheckedIOException e) {
            // BufferedReader#lines() wraps any IOException with an UncheckedIOException, however we wish to explicitly
            // raise and match our method signature.
            throw e.getCause();
        }
    }

    public @NotNull TextFileComponent createFileComponent(@NotNull String text) {
        return new TextFileComponent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return contentType.startsWith(SUPPORTED_CONTENT_TYPE);
    }
}
