package com.hjwylde.bowser.ui.dialogs;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * A custom dialog that displays a preview of a file. This preview functionality is very similar to that of Finder. This
 * dialog defers UI logic to the {@link FilePreview} builder and view.
 */
@NotThreadSafe
public final class FilePreviewDialog {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FilePreviewDialog.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_TITLE = "title";

    private final @NotNull JFrame parent;
    private final @NotNull JDialog dialog;

    private boolean shown = false;

    private FilePreviewDialog(@NotNull JFrame parent, @NotNull Path file) {
        this.parent = parent;

        dialog = buildDialog(file);
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

        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    private @NotNull JDialog buildDialog(@NotNull Path file) {
        JDialog dialog = new JDialog(parent, RESOURCES.getString(RESOURCE_TITLE), true);

        // TODO (hjw): Display a loading component, then replace the loading component with the result
        JPanel panel = new JPanel();
        dialog.getContentPane().add(panel);

        CompletableFuture.supplyAsync(() -> FilePreview.builder().file(file).build())
                .thenAcceptAsync(new OnFilePreviewBuiltConsumer(panel), SwingExecutors.edt());

        return dialog;
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

    @NotThreadSafe
    private final class OnFilePreviewBuiltConsumer implements Consumer<FilePreview.View> {
        private final @NotNull JPanel panel;

        private OnFilePreviewBuiltConsumer(@NotNull JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void accept(FilePreview.View view) {
            panel.removeAll();

            panel.add(view.getComponent());
        }
    }
}
