package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.modules.ExecutorServiceModule;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.dialogs.OpenDialog;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

@NotThreadSafe
final class OpenAction implements Runnable {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(OpenAction.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_BAD_URI = "errorBadUri";

    private final @NotNull TabbedFileBrowser.View view;

    OpenAction(@NotNull TabbedFileBrowser.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        OpenDialog dialog = OpenDialog.builder()
                .parent(view.getComponent())
                .build();

        int result = dialog.show();
        if (result != OpenDialog.OK_OPTION) {
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            try {
                return dialog.getPath();
            } catch (URISyntaxException e) {
                throw new CompletionException(RESOURCES.getString(RESOURCE_ERROR_BAD_URI), e);
            }
        }, ExecutorServiceModule.provideExecutorService()).whenCompleteAsync(
                new OnGetPathConsumer(), SwingExecutors.edt()
        );
    }

    @NotThreadSafe
    private final class OnGetPathConsumer implements BiConsumer<Path, Throwable> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void accept(Path path, Throwable throwable) {
            if (path != null) {
                onSuccess(path);
            } else if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            // throwable is a CompletionException, let's handle the actual cause
            view.handleError(throwable.getCause());
        }

        private void onSuccess(@NotNull Path path) {
            view.addTab(path);
        }
    }
}
