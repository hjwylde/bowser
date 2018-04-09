package com.hjwylde.bowser.ui.views.fileBrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Immutable
final class OpenAction extends AbstractAction {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    private static final @NotNull List<OpenStrategy> DEFAULT_OPEN_STRATEGIES = Arrays.asList(
            new ExtractArchiveStrategy(),
            new OpenWithAssociatedApplicationStrategy()
    );

    private final @NotNull List<OpenStrategy> openStrategies = DEFAULT_OPEN_STRATEGIES;

    private final @NotNull FileBrowser.View view;

    OpenAction(@NotNull FileBrowser.View view) {
        this.view = view;
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
        if (Files.isDirectory(path)) {
            LOGGER.debug("OpenAction called on a directory, doing nothing");
            return;
        }

        try {
            // TODO (hjw): This is delegating to a foreign object, I'm not sure if action events are posted on a
            // background thread, so it would be safer to background it myself.
            openFile(path);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    private void openFile(@NotNull Path file) throws IOException {
        Optional<OpenStrategy> mOpenStrategy = openStrategies.stream()
                .filter(strategy -> strategy.isSupported(file))
                .findFirst();

        if (!mOpenStrategy.isPresent()) {
            return;
        }

        mOpenStrategy.get().openFile(file);
    }
}
