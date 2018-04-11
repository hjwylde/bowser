package com.hjwylde.bowser.ui.frames.bowser;

import com.github.robtimus.filesystems.ftp.FTPEnvironment;
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
import java.util.Optional;

@NotThreadSafe
final class NewFtpTabAction implements Runnable {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(NewFtpTabAction.class.getSimpleName());

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

        Optional<FileSystem> mFileSystem = getFileSystem(dialog);
        if (!mFileSystem.isPresent()) {
            LOGGER.debug("Unable to create a file system from user inputs, doing nothing");
            return;
        }

        view.addTab(mFileSystem.get());
    }

    private Optional<FileSystem> getFileSystem(FtpConnectionDialog dialog) {
        FTPEnvironment env = new FTPEnvironment()
                .withCredentials(dialog.getUsername(), dialog.getPassword());

        try {
            // TODO (hjw): A file system should be closed when finished with, somehow I need to ensure that we close
            // this one.
            URI uri = new URI(dialog.getHost());
            FileSystem fileSystem = FileSystems.newFileSystem(uri, env);

            return Optional.of(fileSystem);
        } catch (IOException | URISyntaxException e) {
            LOGGER.warn(e.getMessage(), e);
        }

        return Optional.empty();
    }
}
