package com.hjwylde.bowser.ui.views.fileBrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@NotThreadSafe
final class OpenAction extends AbstractAction {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    private final @NotNull FileBrowser.View view;

    private final @NotNull List<OpenStrategy> openStrategies;

    OpenAction(@NotNull FileBrowser.View view) {
        this.view = view;

        openStrategies = Arrays.asList(
                new ChangeDirectoryStrategy(view),
                new OpenWithAssociatedApplicationStrategy()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Optional<Path> mPath = view.getSelectedPath();
        if (!mPath.isPresent()) {
            LOGGER.debug("OpenAction called while no path is selected, doing nothing");
            return;
        }

        Path path = mPath.get();
        try {
            // TODO (hjw): This is delegating to a foreign object, I'm not sure if action events are posted on a
            // background thread, so it would be safer to background it myself.
            open(path);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private void open(@NotNull Path path) throws IOException {
        Optional<OpenStrategy> mOpenStrategy = openStrategies.stream()
                .filter(strategy -> strategy.isSupported(path))
                .findFirst();

        if (!mOpenStrategy.isPresent()) {
            LOGGER.debug("No open strategy found for '" + path + "', doing nothing");
            return;
        }

        mOpenStrategy.get().open(path);
    }
}
