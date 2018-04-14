package com.hjwylde.bowser.ui.frames.bowser;

import com.github.robtimus.filesystems.ftp.FTPEnvironment;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.dialogs.FtpConnectionDialog;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ResourceBundle;

@NotThreadSafe
final class NewFtpTabAction implements Runnable {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(NewFtpTabAction.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(NewFtpTabAction.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_UNABLE_TO_CREATE_FILE_SYSTEM = "errorUnableToCreateFileSystem";

    private final @NotNull TabbedFileBrowser.View view;

    NewFtpTabAction(@NotNull TabbedFileBrowser.View view) {
        this.view = view;
    }

    @Override
    public void run() {
        FtpConnectionDialog dialog = FtpConnectionDialog.builder()
                .parent(view.getComponent())
                .build();

        int result = dialog.show();
        if (result != FtpConnectionDialog.OK_OPTION) {
            return;
        }

        try {
            FileSystem fileSystem = getFileSystem(dialog);

            view.addTab(fileSystem);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);

            view.handleError(e);
        }
    }

    private FileSystem getFileSystem(FtpConnectionDialog dialog) throws IOException {
        FTPEnvironment env = new FTPEnvironment()
                .withCredentials(dialog.getUsername(), dialog.getPassword());

        try {
            // TODO (hjw): A file system should be closed when finished with, somehow I need to ensure that we close
            // this one.
            URI uri = new URI(dialog.getHost());

            return FileSystems.newFileSystem(uri, env);
        } catch (IOException | URISyntaxException e) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_UNABLE_TO_CREATE_FILE_SYSTEM), e);
        }
    }
}
