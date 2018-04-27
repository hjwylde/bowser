package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.views.fileDirectory.FileDirectory;
import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.Stack;
import java.util.function.Consumer;

@NotThreadSafe
final class FileBrowserComponent implements FileBrowser.View {
    private final @NotNull JComponent root;

    private final @NotNull FileDirectory.View fileDirectoryView;
    private final @NotNull FilePreview.View filePreviewView;

    // It would be nicer to use the Memento pattern to store the state, however for simplicity this is all we need right
    // now
    private final @NotNull Stack<Path> fileDirectoryState = new Stack<>();

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
    public void navigateBack() {
        // Do nothing if at the root directory
        if (fileDirectoryState.size() <= 1) {
            return;
        }

        fileDirectoryState.pop();

        fileDirectoryView.setDirectory(fileDirectoryState.peek());
    }

    private void initialiseFileDirectoryStateListener() {
        fileDirectoryView.addDirectoryChangeListener(path -> {
            if (fileDirectoryState.empty() || !fileDirectoryState.peek().equals(path)) {
                fileDirectoryState.push(path);
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
}
