package com.github.il4enkodev.binaryformatter;

import com.github.il4enkodev.binaryformatter.bytes.ByteSource;
import com.github.il4enkodev.binaryformatter.bytes.IndexedByteConsumer;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.Formatter;

import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNull;

public class BinaryFormatter implements Closeable, Flushable {

    private Appendable out;
    private IOException error;

    private BackwardsAppendableBuffer buffer;
    private Formatter javaFormatter;

    public BinaryFormatter() {
        this(new StringBuilder());
    }

    public BinaryFormatter(Appendable out) {
        this.out = out != null ? out : new StringBuilder();
        buffer = new BackwardsAppendableBuffer();
    }

    public BinaryFormatter format(BinaryNumberFormat format, long value) {
        try {
            ensureOpen();
            format.write(value, buffer);
            out.append(buffer);
            buffer.reset();
        } catch (IOException e) {
            error = e;
        }
        return this;
    }

    public BinaryFormatter format(BinaryNumberFormat format, byte value) {
        try {
            ensureOpen();
            format.write(value & 0xFF, Byte.SIZE, buffer);
            out.append(buffer);
            buffer.reset();
        } catch (IOException e) {
            error = e;
        }
        return this;
    }

    public BinaryFormatter format(BinaryNumberFormat format, short value) {
        try {
            ensureOpen();
            format.write(value & 0xFFFF, Short.SIZE, buffer);
            out.append(buffer);
            buffer.reset();
        } catch (IOException e) {
            error = e;
        }
        return this;
    }

    public BinaryFormatter format(BinaryNumberFormat format, int value) {
        try {
            ensureOpen();
            format.write(value, Integer.SIZE, buffer);
            out.append(buffer);
            buffer.reset();
        } catch (IOException e) {
            error = e;
        }
        return this;
    }

    public BinaryFormatter format(ByteSourceFormat format, ByteSource source) {
        verifyNonNull(format, "format");
        verifyNonNull(source, "source");
        try {
            ensureOpen();
            source.consume(new IndexedByteFormatter(format, source.maxIndex()));
            newLine();
        } catch (IOException e) {
            error = e;
        }

        return this;
    }

    private final class IndexedByteFormatter implements IndexedByteConsumer {

        private final ByteSourceFormat format;
        private final int maxIndex;
        private int bytePerLineWritten = 0;

        IndexedByteFormatter(ByteSourceFormat format, int maxIndex) {
            this.format = format;
            this.maxIndex = maxIndex;
        }

        @Override
        public boolean accept(int index, byte value) {
            format.write(index, maxIndex, value, buffer);
            try {
                out.append(buffer);
                buffer.reset();
                if (index == maxIndex)
                    return false;

                if (++bytePerLineWritten == format.itemsPerLine()) {
                    newLine();
                    bytePerLineWritten = 0;
                } else {
                    out.append(format.itemsSeparator());
                }

                return true;
            } catch (IOException e) {
                error = e;
                return false;
            }
        }
    }

    public BinaryFormatter newLine() {
        try {
            out().append('\n');
        } catch (IOException e) {
            error = e;
        }
        return this;
    }

    public BinaryFormatter format(String format, Object... args) {
        ensureOpen();
        final Formatter formatter = javaFormatter();
        formatter.format(format, args);
        final IOException ex = formatter.ioException();
        if (ex != null)
            error = ex;
        return this;
    }

    public Appendable out() {
        return out;
    }

    public boolean hasError() {
        return error != null;
    }

    public IOException getError() {
        return error;
    }

    @Override
    public String toString() {
        return out.toString();
    }

    private Formatter javaFormatter() {
        if (javaFormatter == null) {
            javaFormatter = new Formatter(out);
        }
        return javaFormatter;
    }

    private void ensureOpen() {
        if (out == null)
            throw new IllegalStateException("Formatter closed");
    }

    @Override
    public void close() {
        if (out == null)
            return;
        try {
            if (out instanceof Closeable)
                ((Closeable)out).close();
        } catch (IOException e) {
            error = e;
        } finally {
            out = null;
        }
    }

    @Override
    public void flush() {
        ensureOpen();
        if (out instanceof Flushable) {
            try {
                ((Flushable)out).flush();
            } catch (IOException e) {
                error = e;
            }
        }
    }


}
