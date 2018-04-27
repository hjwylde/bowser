package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.views.fileDirectory.FileDirectory;
import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@NotThreadSafe
final class FileBrowserComponent implements FileBrowser.View {
    private final @NotNull JComponent root;

    private final @NotNull FileDirectory.View fileDirectoryView;
    private final @NotNull FilePreview.View filePreviewView;

    // A poor mans state manager. It would be nicer to use the Memento pattern to store the state, however for
    // simplicity this is all we need right now
    private int currentState = -1;
    private @NotNull List<Path> fileDirectoryState = new ArrayList<>();

    FileBrowserComponent(@NotNull JComponent root, @NotNull FileDirectory.View fileDirectoryView,
                         @NotNull FilePreview.View filePreviewView) {
        this.root = root;
        this.fileDirectoryView = fileDirectoryView;
        this.filePreviewView = filePreviewView;

        initialiseFileDirectoryStateListener();
        initialiseFilePreviewListener();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDirectoryChangeListener(@NotNull Consumer<Path> listener) {
        fileDirectoryView.addDirectoryChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getTitle() {
        return fileDirectoryView.getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateBack() {
        // Attempt to navigate up to the parent
        if (currentState <= 0) {
            navigateUp();
            return;
        }

        currentState--;

        fileDirectoryView.setDirectory(fileDirectoryState.get(currentState));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void navigateForward() {
        // Do nothing if at the latest directory
        if (currentState >= fileDirectoryState.size() - 1) {
            return;
        }

        currentState++;

        fileDirectoryView.setDirectory(fileDirectoryState.get(currentState));
    }

    private void initialiseFileDirectoryStateListener() {
        fileDirectoryView.addDirectoryChangeListener(path -> {
            if (fileDirectoryState.isEmpty() || !fileDirectoryState.get(currentState).equals(path)) {
                // Clear the state from this directory onwards; the user is now navigating a different directory tree
                fileDirectoryState.removeAll(fileDirectoryState.subList(currentState + 1, fileDirectoryState.size()));

                fileDirectoryState.add(path);
                currentState++;
            }
        });
    }

    private void initialiseFilePreviewListener() {
        fileDirectoryView.addSelectedPathChangeListener(mPath -> {
            if (mPath.isPresent()) {
                filePreviewView.setFile(mPath.get());
            } else {
                filePreviewView.clearFile();
            }
        });
    }

    private void navigateUp() {
        if (currentState < 0) {
            return;
        }

        Path directory = fileDirectoryState.get(currentState);
        Path parent = directory.getParent();

        // Do nothing if at the root directory
        if (directory.equals(parent)) {
            return;
        }

        fileDirectoryState.add(currentState, parent);
        fileDirectoryView.setDirectory(parent);
    }
}
