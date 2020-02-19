package com.github.il4enkodev.binaryformatter.bytes;

public interface IndexedByteConsumer {
    boolean accept(int index, byte value);
}
