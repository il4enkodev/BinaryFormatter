package com.github.il4enkodev.binaryformatter.bytes;

import java.nio.ByteBuffer;

import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNull;
import static com.github.il4enkodev.binaryformatter.Preconditions.verifyPositive;

public class ByteBufferSource implements ByteSource {

    private final ByteBuffer buffer;
    private final int count;
    private final int initialIndex;

    public ByteBufferSource(ByteBuffer buffer, int count) {
        this.buffer = verifyNonNull(buffer, "buffer");
        this.count = verifyPositive(count, "count");
        initialIndex = buffer.position();
    }

    @Override
    public int minIndex() {
        return initialIndex;
    }

    @Override
    public int maxIndex() {
        return initialIndex + count - 1;
    }

    @Override
    public void consume(IndexedByteConsumer consumer) {
        final int max = initialIndex + count;

    }
}
