package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.actions.open.AbstractOpenAction;
import com.hjwylde.bowser.ui.actions.open.OpenDirectoryInNewTabStrategy;
import com.hjwylde.bowser.ui.actions.open.OpenFileWithAssociatedApplicationStrategy;
import com.hjwylde.bowser.ui.dialogs.OpenDialog;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.ResourceBundle;

@NotThreadSafe
final class OpenAction extends AbstractOpenAction<TabbedFileBrowser.View> {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(OpenAction.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_BAD_URI = "errorBadUri";
    private static final @NotNull String RESOURCE_ERROR_UNSUPPORTED_URI = "errorUnsupportedUri";

    OpenAction(@NotNull TabbedFileBrowser.View view) {
        super(view, Arrays.asList(
                new OpenDirectoryInNewTabStrategy(view),
                new OpenFileWithAssociatedApplicationStrategy()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        OpenDialog dialog = OpenDialog.builder()
                .parent(getView().getComponent())
                .build();

        int result = dialog.show();
        if (result != OpenDialog.OK_OPTION) {
            return;
        }

        try {
            Path path = dialog.getPath();

            openAsync(path);
        } catch (FileSystemNotFoundException e) {
            // TODO (hjw): Sadly FTP file systems will always through this. The file system needs to be created first
            // before it can be found by a URI, but the FTP file system library doesn't properly look up already created
            // file systems.
            Exception e2 = new IOException(RESOURCES.getString(RESOURCE_ERROR_UNSUPPORTED_URI), e);
            LOGGER.warn(e2.getMessage(), e2);

            getView().handleError(e2);
        } catch (URISyntaxException e) {
            Exception e2 = new IOException(RESOURCES.getString(RESOURCE_ERROR_BAD_URI), e);
            LOGGER.warn(e2.getMessage(), e2);

            getView().handleError(e2);
        }
    }
}
