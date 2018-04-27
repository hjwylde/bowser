package com.hjwylde.bowser.ui.dialogs;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * A custom dialog that requests a path from the user. These details are intended to be used for opening a new tab.
 * <p>
 * N.B., no error handling is performed here, it is up to the user of this class to verify the contents of the fields.
 */
@NotThreadSafe
public final class OpenDialog {
    public static final int OK_OPTION = JOptionPane.OK_OPTION;

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(OpenDialog.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_TITLE = "title";
    private static final @NotNull String RESOURCE_PATH = "path";

    private final Component parent;

    private final @NotNull JTextField pathField = new JTextField();

    private boolean shown = false;

    private OpenDialog(Component parent) {
        this.parent = parent;
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
     * Gets the path.
     *
     * @return the path.
     */
    public @NotNull Path getPath() {
        return Paths.get(getPathInput()).toAbsolutePath();
    }

    /**
     * Shows the dialog. This blocks the UI until the user either accepts or cancels. The result can be captured and
     * compared with {@link #OK_OPTION} to determine if it was successful.
     *
     * @return the result.
     */
    public int show() {
        if (shown) {
            throw new IllegalStateException("An OpenDialog may only be shown once.");
        }

        shown = true;

        JLabel pathLabel = new JLabel(RESOURCES.getString(RESOURCE_PATH));

        Object[] message = {pathLabel, pathField};

        return JOptionPane.showConfirmDialog(
                parent,
                message,
                RESOURCES.getString(RESOURCE_TITLE),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Gets the path from the user input.
     *
     * @return the path.
     */
    private @NotNull String getPathInput() {
        return pathField.getText().trim();
    }

    /**
     * An {@link OpenDialog} builder. There are no requirements to building an {@link OpenDialog}.
     */
    @NotThreadSafe
    public static final class Builder {
        private Component parent;

        private Builder() {
        }

        /**
         * Builds the dialog and returns it. The dialog is not visible until you call {@link #show()}.
         *
         * @return the dialog.
         */
        public @NotNull OpenDialog build() {
            return new OpenDialog(parent);
        }

        /**
         * Set the parent of the dialog. This is used when creating it, but is not necessary.
         *
         * @param parent the parent of the dialog.
         * @return this builder.
         */
        public @NotNull Builder parent(Component parent) {
            this.parent = parent;

            return this;
        }
    }
}
