package com.hjwylde.bowser.io;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@NotThreadSafe
public final class RestrictedInputStream extends FilterInputStream {
    private final int limit;

    private int count;
    private int mark;

    public RestrictedInputStream(@NotNull InputStream in, int limit) {
        super(in);

        this.limit = limit;
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);

        mark = count;
    }

    @Override
    public int read(@NotNull byte[] b) throws IOException {
        int r = super.read(b);
        if (r > 0) {
            count += r;
            checkLimit();
        }

        return r;
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        int r = super.read(b, off, len);
        if (r > 0) {
            count += r;
            checkLimit();
        }

        return r;
    }

    @Override
    public int read() throws IOException {
        int r = super.read();
        if (r > 0) {
            count++;
            checkLimit();
        }

        return r;
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();

        count = mark;
    }

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
