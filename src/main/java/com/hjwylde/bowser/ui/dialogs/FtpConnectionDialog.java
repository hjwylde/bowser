package com.hjwylde.bowser.ui.dialogs;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * A custom dialog that requests a host, username and password from the user. These details are intended to be used in
 * connecting to an FTP server.
 * <p>
 * N.B., no error handling is performed here, it is up to the user of this class to verify the contents of the fields.
 */
@NotThreadSafe
public final class FtpConnectionDialog {
    public static final int OK_OPTION = JOptionPane.OK_OPTION;

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FtpConnectionDialog.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_HOST = "host";
    private static final @NotNull String RESOURCE_PASSWORD = "password";
    private static final @NotNull String RESOURCE_TITLE = "title";
    private static final @NotNull String RESOURCE_USERNAME = "username";

    private static final @NotNull String DEFAULT_USERNAME = "anonymous";

    private final Component parent;

    private final @NotNull JTextField hostField = new JTextField();
    private final @NotNull JTextField usernameField = new JTextField();
    private final @NotNull JPasswordField passwordField = new JPasswordField();

    private boolean shown = false;

    private FtpConnectionDialog(Component parent) {
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
     * Gets the host from the user input.
     *
     * @return the host.
     */
    public @NotNull String getHost() {
        return hostField.getText().trim();
    }

    /**
     * Gets the password from the user input.
     *
     * @return the password.
     */
    public @NotNull char[] getPassword() {
        return passwordField.getPassword();
    }

    /**
     * Gets the username from the user input, or a default ("anonymous") if absent.
     *
     * @return the username or "anonymous".
     */
    public @NotNull String getUsername() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            username = DEFAULT_USERNAME;
        }
        return username;
    }


    /**
     * Shows the dialog. This blocks the UI until the user either accepts or cancels. The result can be captured and
     * compared with {@link #OK_OPTION} to determine if it was successful.
     *
     * @return the result.
     */
    public int show() {
        if (shown) {
            throw new IllegalStateException("An FtpConnectionDialog may only be shown once.");
        }

        shown = true;

        JLabel hostLabel = new JLabel(RESOURCES.getString(RESOURCE_HOST));
        JLabel usernameLabel = new JLabel(RESOURCES.getString(RESOURCE_USERNAME));
        JLabel passwordLabel = new JLabel(RESOURCES.getString(RESOURCE_PASSWORD));

        Object[] message = {hostLabel, hostField, usernameLabel, usernameField, passwordLabel, passwordField};

        return JOptionPane.showConfirmDialog(
                parent,
                message,
                RESOURCES.getString(RESOURCE_TITLE),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * An {@link FtpConnectionDialog} builder. There are no requirements to building an {@link FtpConnectionDialog}.
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
        public @NotNull FtpConnectionDialog build() {
            return new FtpConnectionDialog(parent);
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
