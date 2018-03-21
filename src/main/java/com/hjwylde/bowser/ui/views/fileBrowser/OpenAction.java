package com.hjwylde.bowser.ui.views.fileBrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

final class OpenAction extends AbstractAction {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    private static final @NotNull List<OpenStrategy> DEFAULT_OPEN_STRATEGIES = Arrays.asList(
            new ExtractArchiveStrategy(),
            new OpenWithAssociatedApplicationStrategy()
    );

    private final @NotNull List<OpenStrategy> openStrategies;

    OpenAction() {
        this(DEFAULT_OPEN_STRATEGIES);
    }

    OpenAction(@NotNull List<OpenStrategy> openStrategies) {
        this.openStrategies = Objects.requireNonNull(new ArrayList<>(openStrategies));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (!(event.getSource() instanceof JTree)) {
            throw new IllegalArgumentException("event source must be an instance of JTree, instead it was " + event.getSource().getClass());
        }

        JTree tree = (JTree) event.getSource();
        FileTreeNode node = (FileTreeNode) tree.getLastSelectedPathComponent();
        if (node.isDirectory()) {
            return;
        }

        Path file = node.getFilePath();
        try {
            openFile(file);
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
