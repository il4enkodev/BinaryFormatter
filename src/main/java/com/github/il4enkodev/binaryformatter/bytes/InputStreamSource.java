package com.github.il4enkodev.binaryformatter.bytes;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNegative;
import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNull;

public class InputStreamSource implements ByteSource, Closeable {

    private final BufferedInputStream stream;
    private final int count;
    private final int initialIndex;

    public InputStreamSource(InputStream stream, int count) {
        this(stream, 0, count);
    }

    public InputStreamSource(InputStream stream, int initialIndex, int count) {
        this.stream = new BufferedInputStream(verifyNonNull(stream, "stream"));
        this.count = verifyNonNegative(count, "count");
        this.initialIndex = verifyNonNegative(initialIndex, "initialIndex");
        this.stream.mark(count);
    }

    @Override
    public int minIndex() {
        return initialIndex;
    }

    @Override
    public int maxIndex() {
        return initialIndex + count;
    }

    @Override
    public void consume(IndexedByteConsumer consumer) throws IOException {
        verifyNonNull(consumer, "consumer");
        int idx = initialIndex;
        stream.reset();
        for (int read = 0, next; read < count && (next = stream.read()) != -1; read++) {
            if (!(consumer.accept(idx++, (byte) next)))
                break;
        }
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
