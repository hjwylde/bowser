package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
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
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

@NotThreadSafe
final class OpenAction extends AbstractAction {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(OpenAction.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_NO_OPEN_STRATEGY_FOUND = "errorNoOpenStrategyFound";

    private final @NotNull FileDirectory.View view;

    private final @NotNull List<OpenStrategy> openStrategies;

    OpenAction(@NotNull FileDirectory.View view) {
        this.view = view;

        openStrategies = Arrays.asList(
                new BrowseDirectoryStrategy(view),
                new BrowseArchiveStrategy(view),
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

        CompletableFuture.runAsync(() -> {
            Path path = mPath.get();
            try {
                open(path);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }).whenCompleteAsync(new OnOpenConsumer(), SwingExecutors.edt());
    }

    private void open(@NotNull Path path) throws IOException {
        Optional<OpenStrategy> mOpenStrategy = openStrategies.stream()
                .filter(strategy -> strategy.isSupported(path))
                .findFirst();

        if (!mOpenStrategy.isPresent()) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_NO_OPEN_STRATEGY_FOUND));
        }

        OpenStrategy openStrategy = mOpenStrategy.get();

        openStrategy.open(path);
    }

    @NotThreadSafe
    private final class OnOpenConsumer implements BiConsumer<Void, Throwable> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void accept(Void aVoid, Throwable throwable) {
            if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            // throwable is a CompletionException, let's handle the actual cause
            view.handleError(throwable.getCause());
        }
    }
}
