package com.github.il4enkodev.binaryformatter;

import java.util.Locale;

public class BinaryNumberFormat extends Format {

    private static final char one = '1';

    public BinaryNumberFormat() {
    }

    public BinaryNumberFormat(Locale locale) {
        super(locale);
    }

    @Override
    public BinaryNumberFormat groupSize(int groupSize) {
        super.groupSize(groupSize);
        return this;
    }

    @Override
    public BinaryNumberFormat groupingSeparator(char groupingSeparator) {
        super.groupingSeparator(groupingSeparator);
        return this;
    }

    @Override
    public BinaryNumberFormat minWidth(int minWidth) {
        super.minWidth(minWidth);
        return this;
    }

    @Override
    public BinaryNumberFormat prefix(CharSequence prefix) {
        super.prefix(prefix);
        return this;
    }

    @Override
    public BinaryNumberFormat suffix(CharSequence suffix) {
        super.suffix(suffix);
        return this;
    }

    @Override
    public BinaryNumberFormat alignment(Alignment alignment) {
        super.alignment(alignment);
        return this;
    }

    void write(int value, int digitsCount, BackwardsAppendableBuffer buffer) {
        final int spaces = spacesCount(digitsCount);
        writeSuffixAndSpaces(spaces, buffer);
        writeValue(value, digitsCount, buffer);
        writePrefixAndSpaces(spaces, buffer);
    }

    void write(long value, BackwardsAppendableBuffer buffer) {
        final int spaces = spacesCount(Long.SIZE);
        writeSuffixAndSpaces(spaces, buffer);
        writeValue(value, buffer);
        writePrefixAndSpaces(spaces, buffer);
    }

    private void writeValue(long value, BackwardsAppendableBuffer buffer) {
        int digitsCount = Long.SIZE;
        long mask = 1L;
        int groupIdx = 0;

        final int groupSize = groupSize();
        final char groupingSeparator = groupingSeparator();

        while (digitsCount > 0) {
            buffer.append((value & mask) == 0 ? zero : one);
            mask <<= 1;
            if (--digitsCount > 0 && ++groupIdx == groupSize) {
                groupIdx = 0;
                buffer.append(groupingSeparator);
            }
        }
    }

    private void writeValue(int value, int digitsCount, BackwardsAppendableBuffer buffer) {
        int mask = 1;
        int groupIdx = 0;

        final int groupSize = groupSize();
        final char groupingSeparator = groupingSeparator();

        while (digitsCount > 0) {
            buffer.append((value & mask) == 0 ? zero : one);
            mask <<= 1;
            if (--digitsCount > 0 && ++groupIdx == groupSize) {
                groupIdx = 0;
                buffer.append(groupingSeparator);
            }
        }
    }

    private int spacesCount(int digitsCount) {
        final int contentWidth = contentWidth(digitsCount);
        final int width = Math.max(minWidth(), contentWidth);
        return width - contentWidth;
    }

}
