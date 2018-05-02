package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.ui.actions.open.AbstractOpenAction;
import com.hjwylde.bowser.ui.actions.open.BrowseArchiveStrategy;
import com.hjwylde.bowser.ui.actions.open.BrowseDirectoryStrategy;
import com.hjwylde.bowser.ui.actions.open.OpenFileWithAssociatedApplicationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@NotThreadSafe
final class OpenAction extends AbstractOpenAction<FileDirectory.View> {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    OpenAction(@NotNull FileDirectory.View view) {
        super(view, Arrays.asList(
                new BrowseDirectoryStrategy(view),
                new BrowseArchiveStrategy(view),
                new OpenFileWithAssociatedApplicationStrategy()
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        Optional<Path> mPath = getView().getSelectedPath();
        if (!mPath.isPresent()) {
            LOGGER.debug("OpenAction called while no path is selected, doing nothing");
            return;
        }

        Path path = mPath.get();

        openAsync(path);
    }
}
