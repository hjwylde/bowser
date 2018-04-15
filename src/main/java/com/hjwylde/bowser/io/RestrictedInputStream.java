package com.hjwylde.bowser.io;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An {@link InputStream} that counts and limits the number of bytes that are read. The main use case for this class is
 * to help support and prevent reading files that would be too large to hold in memory.
 */
@NotThreadSafe
public final class RestrictedInputStream extends FilterInputStream {
    private final int limit;

    private int count = 0;
    private int mark = 0;

    /**
     * Creates a new {@link RestrictedInputStream} that wraps the given one, with the given limit (in bytes).
     *
     * @param in    the input stream to wrap.
     * @param limit the read limit in bytes.
     */
    public RestrictedInputStream(@NotNull InputStream in, int limit) {
        super(in);

        this.limit = limit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);

        mark = count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(@NotNull byte[] b) throws IOException {
        int r = super.read(b);
        if (r >= 0) {
            count += r;
            checkLimit();
        }

        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        int r = super.read(b, off, len);
        if (r >= 0) {
            count += r;
            checkLimit();
        }

        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        int r = super.read();
        if (r >= 0) {
            count++;
            checkLimit();
        }

        return r;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void reset() throws IOException {
        super.reset();

        count = mark;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long skip(long n) throws IOException {
        long l = super.skip(n);

        count += l;
        checkLimit();

        return l;
    }

    private void checkLimit() throws IOException {
        if (count > limit) {
            throw new IOException("InputStream limit of " + limit + " exceeded: " + count);
        }
    }
}
