package com.hjwylde.bowser.ui.views.filePreview;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

/**
 * {@link FilePreview} provides the interfaces to create and use a component that previews a file. The preview is very
 * simple for now, it supports only viewing. In the future, it would be nice to add features such as navigating to
 * neighbouring files, and closing the preview with shortcuts.
 */
public final class FilePreview {
    private FilePreview() {
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
        void clearFile();

        void setFile(@NotNull Path file);
    }

    @Immutable
    public static final class Builder {
        private Builder() {
        }

        /**
         * Builds and returns a new {@link View}.
         *
         * @return a new {@link View}.
         */
        public @NotNull View build() {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);

            return new FilePreviewComponent(panel);
        }
    }
}
