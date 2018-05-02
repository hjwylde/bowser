package com.hjwylde.bowser.ui.actions.open;

import com.hjwylde.bowser.modules.ExecutorServiceModule;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.View;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

@NotThreadSafe
abstract public class AbstractOpenAction<V extends View> implements Runnable {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(AbstractOpenAction.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(AbstractOpenAction.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_NO_OPEN_STRATEGY_FOUND = "errorNoOpenStrategyFound";

    private final @NotNull V view;

    private final @NotNull List<OpenStrategy> openStrategies;

    protected AbstractOpenAction(@NotNull V view, @NotNull List<OpenStrategy> openStrategies) {
        this.view = view;

        this.openStrategies = new ArrayList<>(openStrategies);
    }

    protected @NotNull V getView() {
        return view;
    }

    protected void openAsync(@NotNull Path path) {
        CompletableFuture.runAsync(() -> {
            try {
                open(path);
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        }, ExecutorServiceModule.provideExecutorService()).whenCompleteAsync(
                new OnOpenConsumer(), SwingExecutors.edt()
        );
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
