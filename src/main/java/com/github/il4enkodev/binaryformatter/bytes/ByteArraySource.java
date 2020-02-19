package com.github.il4enkodev.binaryformatter.bytes;

import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNegative;
import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNull;

public final class ByteArraySource implements ByteSource {

    private final byte[] array;
    private final int fromIndex;
    private final int count;

    private final int maxIndex;

    public ByteArraySource(byte[] array) {
        this.array = verifyNonNull(array, "array");
        fromIndex = 0;
        count = array.length;
        maxIndex = fromIndex + count;
    }

    public ByteArraySource(byte[] array, int count) {
        this.array = verifyNonNull(array, "array");
        this.count = verifyNonNegative(count, "count");
        if (count > array.length)
            throw new IndexOutOfBoundsException("count: " + count + ", array.length: " + array.length);
        fromIndex = 0;
        maxIndex = count - 1;
    }

    public ByteArraySource(byte[] array, int fromIndex, int count) {
        this.array = verifyNonNull(array, "array");
        this.fromIndex = verifyNonNegative(fromIndex, "fromIndex");
        this.count = verifyNonNegative(count, "count");
        maxIndex = fromIndex + count - 1;
        if (maxIndex > array.length)
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex +
                    ", count: " + count + ", array.length: " + array.length);
    }

    @Override
    public int minIndex() {
        return fromIndex;
    }

    @Override
    public int maxIndex() {
        return maxIndex;
    }

    @Override
    public void consume(IndexedByteConsumer consumer) {
        for (int i = fromIndex; i < count; i++) {
            if (!(consumer.accept(i, array[i])))
                break;
        }
    }

}
