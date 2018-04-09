package com.hjwylde.bowser.ui.dialogs;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.ResourceBundle;

/**
 * A custom dialog that displays a preview of a file. This preview functionality is very similar to that of Finder. This
 * dialog defers UI logic to the {@link FilePreview} builder and view.
 */
@NotThreadSafe
public final class FilePreviewDialog {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FilePreviewDialog.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_TITLE = "title";

    private final @NotNull JFrame parent;

    private final @NotNull Path file;

    private boolean shown = false;

    private FilePreviewDialog(@NotNull JFrame parent, @NotNull Path file) {
        this.parent = parent;
        this.file = file;
    }

    /**
     * Returns a new dialog builder.
     *
     * @return a new dialog builder.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Shows the dialog. This blocks the UI until the user closes the preview. This method may only be called once. Any
     * repeated calls will result in a {@link IllegalStateException}.
     *
     * @throws IllegalStateException if called repeatedly.
     */
    public void show() {
        if (shown) {
            throw new IllegalStateException("A FilePreviewDialog may only be shown once.");
        }

        shown = true;

        JDialog dialog = new JDialog(parent, RESOURCES.getString(RESOURCE_TITLE), true);

        // TODO (hjw): Display a loading component, background the actual IO operations, then replace the loading
        // component with the result
        FilePreview.View filePreviewView = FilePreview.builder()
                .file(file)
                .build();

        dialog.getContentPane().add(filePreviewView.getComponent());

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    /**
     * An {@link FilePreviewDialog} builder. There are no requirements to building an {@link FilePreviewDialog}.
     */
    @NotThreadSafe
    public static final class Builder {
        private JFrame parent;

        private Path file;

        private Builder() {
        }

        /**
         * Builds the dialog and returns it. The dialog is not visible until you call {@link #show()}.
         *
         * @return the dialog.
         */
        public @NotNull FilePreviewDialog build() {
            return new FilePreviewDialog(parent, file);
        }

        /**
         * Set the file to preview. This is required.
         *
         * @param file the file to preview.
         * @return this builder.
         */
        public @NotNull Builder file(@NotNull Path file) {
            this.file = file;

            return this;
        }

        /**
         * Set the parent of the dialog. This is used when creating it, but is not necessary.
         *
         * @param parent the parent of the dialog.
         * @return this builder.
         */
        public @NotNull Builder parent(JFrame parent) {
            this.parent = parent;

            return this;
        }
    }
}
