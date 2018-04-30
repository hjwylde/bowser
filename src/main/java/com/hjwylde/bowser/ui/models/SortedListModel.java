package com.hjwylde.bowser.ui.models;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@NotThreadSafe
public final class SortedListModel<E> extends AbstractListModel<E> {
    private @NotNull List<E> elements = new ArrayList<E>();
    private @NotNull Comparator<E> comparator;

    public SortedListModel(@NotNull Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public void add(@NotNull E fileNode) {
        elements.add(fileNode);

        sort();
    }

    public void clear() {
        elements.clear();

        fireContentsChanged(this, 0, elements.size());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public E getElementAt(int index) {
        return elements.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return elements.size();
    }

    public void setComparator(@NotNull Comparator<E> comparator) {
        this.comparator = comparator;

        sort();
    }

    private void sort() {
        elements.sort(comparator);

        fireContentsChanged(this, 0, elements.size());
    }
}
