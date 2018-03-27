package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.ui.views.fileComponents.EmptyFileComponentFactory;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponentFactory;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponentFactoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * {@link FilePreview} provides the interfaces to create and use a component that previews a file. The preview is very
 * simple for now, it supports only viewing. In the future, it would be nice to add features such as navigating to
 * neighbouring files, and closing the preview with shortcuts.
 */
public final class FilePreview {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FilePreview.class.getSimpleName());

    private FilePreview() {
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
    }

    public static final class Builder {
        private Path file;

        private Builder() {
        }

        /**
         * Builds and returns a new {@link View}. The file preview must have a file path to reference.
         *
         * @return a new {@link View}.
         * @throws IllegalStateException if file is null.
         */
        public @NotNull View build() {
            if (file == null) {
                throw new IllegalStateException("file must be set.");
            }

            FileComponent fileComponent = buildFileComponent()
                    .orElse(buildEmptyFileComponent());

            return new FilePreviewComponent(fileComponent);
        }

        public @NotNull Builder file(@NotNull Path file) {
            this.file = Objects.requireNonNull(file, "file cannot be null.");

            return this;
        }

        private @NotNull FileComponent buildEmptyFileComponent() {
            FileComponentFactory<? extends FileComponent> fileComponentFactory = getEmptyFileComponentFactory();

            return fileComponentFactory.createFileComponent(new byte[0]);
        }

        private @NotNull Optional<FileComponent> buildFileComponent() {
            if (Files.isDirectory(file)) {
                return Optional.empty();
            }

            try {
                // TODO (hjw): This is potentially quite bad, it would be better if the factory took an input stream and
                // dealt with potentially large files itself, rather than introducing the possibility of running out of
                // memory by loading the entire file here.
                FileComponentFactory fileComponentFactory = getFileComponentFactory();
                byte[] data = Files.readAllBytes(file);

                FileComponent fileComponent = fileComponentFactory.createFileComponent(data);

                return Optional.of(fileComponent);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }

            return Optional.empty();
        }

        private @NotNull FileComponentFactory<? extends FileComponent> getEmptyFileComponentFactory() {
            return EmptyFileComponentFactory.getInstance();
        }

        private @NotNull FileComponentFactory getFileComponentFactory() {
            return FileComponentFactoryService.getInstance()
                    .getFileComponentFactory(file)
                    .orElse(getEmptyFileComponentFactory());
        }
    }
}
