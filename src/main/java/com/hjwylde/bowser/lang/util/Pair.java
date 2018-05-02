package com.hjwylde.bowser.lang.util;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * An immutable pair of objects.
 *
 * @param <F> the type of the first object.
 * @param <S> the type of the second object.
 */
@Immutable
public final class Pair<F, S> {
    private final @NotNull F first;
    private final @NotNull S second;

    /**
     * Creates a new pair of the given objects.
     *
     * @param first  the first object.
     * @param second the second object.
     */
    public Pair(@NotNull F first, @NotNull S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Pair<?, ?>)) {
            return false;
        }

        Pair<?, ?> pair = (Pair<?, ?>) obj;

        return first.equals(pair.first) && second.equals(pair.second);
    }

    /**
     * Gets the first object.
     *
     * @return the first object.
     */
    public @NotNull F getFirst() {
        return first;
    }

    /**
     * Gets the second object.
     *
     * @return the second object.
     */
    public @NotNull S getSecond() {
        return second;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
