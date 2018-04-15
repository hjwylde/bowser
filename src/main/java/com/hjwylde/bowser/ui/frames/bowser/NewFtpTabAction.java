package com.hjwylde.bowser.ui.frames.bowser;

import com.github.robtimus.filesystems.ftp.FTPEnvironment;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.dialogs.FtpConnectionDialog;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

@NotThreadSafe
final class NewFtpTabAction implements Runnable {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(NewFtpTabAction.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(NewFtpTabAction.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_BAD_HOST = "errorBadHost";
    private static final @NotNull String RESOURCE_ERROR_UNABLE_TO_CREATE_FILE_SYSTEM = "errorUnableToCreateFileSystem";

    private static final @NotNull List<String> SUPPORTED_FTP_SCHEMES = Arrays.asList("ftp", "ftps");

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

        CompletableFuture.supplyAsync(() -> {
            try {
                return getFileSystem(dialog);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }).whenCompleteAsync(new OnGetFileSystemConsumer(), SwingExecutors.edt());
    }

    private FileSystem getFileSystem(FtpConnectionDialog dialog) throws IOException {
        try {
            // TODO (hjw): A file system should be closed when finished with, somehow I need to ensure that we close
            // this one.
            // Trim trailing "/"s; the FtpFileSystem library considers them a path and does not support it.
            String host = dialog.getHost().replaceAll("/+$", "");
            URI uri = new URI(host);

            // We need to manually check the scheme here. FileSystems assumes that the scheme is non-null, but
            // unfortunately an input such as "foo" is considered a valid URI, but has no scheme.
            if (!SUPPORTED_FTP_SCHEMES.contains(uri.getScheme())) {
                throw new URISyntaxException(dialog.getHost(), "Unsupported URI scheme");
            }

            // TODO (hjw): There is a bug in the FTPFileSystemProvider which prevents a file system from being re-used
            // properly. FTPFileSystemProvider#getFileSystem(URI) performs a lookup with a normalisation method
            // different to that of #newFileSystem.
            try {
                return FileSystems.getFileSystem(uri);
            } catch (FileSystemNotFoundException ignored) {
                FTPEnvironment env = new FTPEnvironment()
                        .withCredentials(dialog.getUsername(), dialog.getPassword());

                return FileSystems.newFileSystem(uri, env);
            }
        } catch (URISyntaxException e) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_BAD_HOST), e);
        } catch (IOException e) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_UNABLE_TO_CREATE_FILE_SYSTEM), e);
        }
    }

    @NotThreadSafe
    private final class OnGetFileSystemConsumer implements BiConsumer<FileSystem, Throwable> {
        @Override
        public void accept(FileSystem fileSystem, Throwable throwable) {
            if (fileSystem != null) {
                onSuccess(fileSystem);
            } else if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            // throwable is a CompletionException, let's handle the actual cause
            view.handleError(throwable.getCause());
        }

        private void onSuccess(@NotNull FileSystem fileSystem) {
            view.addTab(fileSystem);
        }
    }
}
