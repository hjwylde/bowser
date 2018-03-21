package com.hjwylde.bowser.ui.dialogs;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public final class FtpConnectionDialog {
    public static final int OK_OPTION = JOptionPane.OK_OPTION;

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FtpConnectionDialog.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_HOST = "host";
    private static final @NotNull String RESOURCE_PASSWORD = "password";
    private static final @NotNull String RESOURCE_TITLE = "title";
    private static final @NotNull String RESOURCE_USERNAME = "username";

    private final Component parent;

    private final @NotNull JTextField hostField = new JTextField();
    private final @NotNull JTextField usernameField = new JTextField();
    private final @NotNull JPasswordField passwordField = new JPasswordField();

    private FtpConnectionDialog(Component parent) {
        this.parent = parent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public @NotNull String getHost() {
        return hostField.getText().trim();
    }

    public @NotNull char[] getPassword() {
        return passwordField.getPassword();
    }

    public @NotNull String getUsername() {
        return usernameField.getText().trim();
    }

    public int show() {
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

    public static final class Builder {
        private Component parent;

        private Builder() {
        }

        public FtpConnectionDialog build() {
            return new FtpConnectionDialog(parent);
        }

        public Builder parent(Component parent) {
            this.parent = parent;

            return this;
        }
    }
}
