package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.modules.ExecutorServiceModule;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponentFactory;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponentFactoryService;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.BiConsumer;

@NotThreadSafe
final class FilePreviewComponent implements FilePreview.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FilePreviewComponent.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FilePreviewComponent.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_PREVIEWING_DIRECTORY = "errorPreviewingDirectory";

    private static final @NotNull FileComponent EMPTY_FILE_COMPONENT = buildEmptyFileComponent();

    private final @NotNull JPanel panel;

    FilePreviewComponent(@NotNull JPanel panel) {
        this.panel = panel;

        setFileComponent(EMPTY_FILE_COMPONENT);
    }

    private static @NotNull FileComponent buildEmptyFileComponent() {
        EmptyFileComponentFactory fileComponentFactory = EmptyFileComponentFactory.getInstance();

        return fileComponentFactory.createFileComponent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearFile() {
        setFileComponent(EMPTY_FILE_COMPONENT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return panel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFile(@NotNull Path file) {
        CompletableFuture.supplyAsync(() -> buildFileComponent(file), ExecutorServiceModule.provideExecutorService())
                .whenCompleteAsync(new OnFileComponentBuiltConsumer(), SwingExecutors.edt());
    }

    private @NotNull FileComponent buildFileComponent(@NotNull Path file) {
        if (Files.isDirectory(file)) {
            Exception e = new IllegalArgumentException(RESOURCES.getString(RESOURCE_ERROR_PREVIEWING_DIRECTORY));

            throw new CompletionException(e);
        }

        FileComponentFactory fileComponentFactory = getFileComponentFactory(file);
        try (InputStream in = Files.newInputStream(file)) {
            return fileComponentFactory.createFileComponent(in);
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    private @NotNull FileComponentFactory getFileComponentFactory(@NotNull Path file) {
        return FileComponentFactoryService.getInstance()
                .getFileComponentFactory(file)
                .orElse(EmptyFileComponentFactory.getInstance());
    }

    private void setFileComponent(@NotNull FileComponent fileComponent) {
        panel.removeAll();
        panel.add(fileComponent.getComponent());

        panel.revalidate();
        panel.repaint();
    }

    @NotThreadSafe
    private final class OnFileComponentBuiltConsumer implements BiConsumer<FileComponent, Throwable> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void accept(FileComponent fileComponent, Throwable throwable) {
            if (fileComponent != null) {
                onSuccess(fileComponent);
            } else if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.trace(throwable.getMessage(), throwable);

            setFileComponent(EMPTY_FILE_COMPONENT);
        }

        private void onSuccess(@NotNull FileComponent fileComponent) {
            setFileComponent(fileComponent);
        }
    }
}
