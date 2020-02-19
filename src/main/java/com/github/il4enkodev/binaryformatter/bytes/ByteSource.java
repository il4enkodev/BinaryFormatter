package com.github.il4enkodev.binaryformatter.bytes;

import java.io.IOException;

public interface ByteSource {
    int minIndex();
    int maxIndex();
    void consume(IndexedByteConsumer consumer) throws IOException;
}
