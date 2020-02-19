package com.github.il4enkodev.binaryformatter;

import java.util.Locale;

public class IndexFormat extends Format {

    public static final IndexFormat WITHOUT_INDEXES = new WithoutIndexes();

    public static IndexFormat getDefault() {
        return new IndexFormat().prefix("[").suffix("]");
    }

    public IndexFormat() {
    }

    public IndexFormat(Locale locale) {
        super(locale);
    }

    @Override
    public IndexFormat groupSize(int groupSize) {
        super.groupSize(groupSize);
        return this;
    }

    @Override
    public IndexFormat groupingSeparator(char groupingSeparator) {
        super.groupingSeparator(groupingSeparator);
        return this;
    }

    @Override
    public IndexFormat minWidth(int minWidth) {
        super.minWidth(minWidth);
        return this;
    }

    @Override
    public IndexFormat prefix(CharSequence prefix) {
        super.prefix(prefix);
        return this;
    }

    @Override
    public IndexFormat suffix(CharSequence suffix) {
        super.suffix(suffix);
        return this;
    }

    @Override
    public IndexFormat alignment(Alignment alignment) {
        super.alignment(alignment);
        return this;
    }

    int spacesCount(int index, int maxIndex) {
        final int contentWidth = contentWidth(digitsCount(index));
        final int width = Math.max(minWidth(), contentWidth(digitsCount(maxIndex)));
        return width - contentWidth;
    }

    void write(int index, int maxIndex, BackwardsAppendableBuffer buffer) {
        final int spaces = spacesCount(index, maxIndex);
        writeSuffixAndSpaces(spaces, buffer);
        writeValue(index, buffer);
        writePrefixAndSpaces(spaces, buffer);
    }

    void writeValue(int value, BackwardsAppendableBuffer buffer) {
        int q, r;

        int groupIdx = 0;
        final int groupSize = groupSize();
        final char groupingSeparator = groupingSeparator();

        while (value >= 65536) {
            q = value / 100;
            r = value - ((q << 6) + (q << 5) + (q << 2));
            value = q;

            buffer.append(DigitOnes[r]);
            if (++groupIdx == groupSize) {
                groupIdx = 0;
                buffer.append(groupingSeparator);
            }

            buffer.append(DigitTens[r]);
            if (++groupIdx == groupSize) {
                groupIdx = 0;
                buffer.append(groupingSeparator);
            }
        }

        do {
            q = (value * 52429) >>> (16 + 3);
            r = value - ((q << 3) + (q << 1));
            buffer.append(digits[r]);

            value = q;
            if (++groupIdx == groupSize && value != 0) {
                groupIdx = 0;
                buffer.append(groupingSeparator);
            }
        } while (value != 0);
    }

    static int digitsCount(int x) {
        for (int i = 0; ; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    private static final class WithoutIndexes extends IndexFormat {

        @Override
        void write(int index, int digitsCount, BackwardsAppendableBuffer buffer) {
            // do nothing
        }

        @Override
        public int groupSize() {
            throw new UnsupportedOperationException();
        }

        @Override
        public char groupingSeparator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public IndexFormat groupSize(int groupSize) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IndexFormat groupingSeparator(char groupingSeparator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IndexFormat minWidth(int minWidth) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IndexFormat prefix(CharSequence prefix) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IndexFormat suffix(CharSequence suffix) {
            throw new UnsupportedOperationException();
        }

        @Override
        public IndexFormat alignment(Alignment alignment) {
            throw new UnsupportedOperationException();
        }
    }

    final static char[] DigitTens = {
            '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
            '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2',
            '3', '3', '3', '3', '3', '3', '3', '3', '3', '3',
            '4', '4', '4', '4', '4', '4', '4', '4', '4', '4',
            '5', '5', '5', '5', '5', '5', '5', '5', '5', '5',
            '6', '6', '6', '6', '6', '6', '6', '6', '6', '6',
            '7', '7', '7', '7', '7', '7', '7', '7', '7', '7',
            '8', '8', '8', '8', '8', '8', '8', '8', '8', '8',
            '9', '9', '9', '9', '9', '9', '9', '9', '9', '9',
    };

    final static char[] DigitOnes = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    final static int[] sizeTable =
            { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };
}