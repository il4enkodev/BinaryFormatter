package com.github.il4enkodev.binaryformatter;

import java.util.Locale;

public final class ByteSourceFormat extends BinaryNumberFormat {

    private CharSequence itemsSeparator = " ";
    private int itemsPerLine = 10;

    private IndexFormat indexFormat = IndexFormat.WITHOUT_INDEXES;

    public ByteSourceFormat() {
    }

    public ByteSourceFormat(Locale locale) {
        super(locale);
    }

    public ByteSourceFormat indexFormat(IndexFormat indexFormat) {
        this.indexFormat = Preconditions.verifyNonNull(indexFormat, "indexFormat");
        return this;
    }

    public ByteSourceFormat itemsSeparator(CharSequence separator) {
        this.itemsSeparator = separator != null ? separator : "";
        return this;
    }

    public ByteSourceFormat itemsPerLine(int itemsPerLine) {
        this.itemsPerLine = Preconditions.verifyPositive(itemsPerLine, "itemsPerLine");
        return this;
    }

    public IndexFormat indexFormat() {
        return indexFormat;
    }

    public int itemsPerLine() {
        return itemsPerLine;
    }

    public CharSequence itemsSeparator() {
        return itemsSeparator;
    }

    @Override
    public ByteSourceFormat groupSize(int groupSize) {
        super.groupSize(groupSize);
        return this;
    }

    @Override
    public ByteSourceFormat groupingSeparator(char groupingSeparator) {
        super.groupingSeparator(groupingSeparator);
        return this;
    }

    @Override
    public ByteSourceFormat minWidth(int minWidth) {
        super.minWidth(minWidth);
        return this;
    }

    @Override
    public ByteSourceFormat prefix(CharSequence prefix) {
        super.prefix(prefix);
        return this;
    }

    @Override
    public ByteSourceFormat suffix(CharSequence suffix) {
        super.suffix(suffix);
        return this;
    }

    @Override
    public ByteSourceFormat alignment(Alignment alignment) {
        super.alignment(alignment);
        return this;
    }

    void write(int index, int maxIndex, int value, BackwardsAppendableBuffer buffer) {
        write(value, Byte.SIZE, buffer);
        indexFormat.write(index, maxIndex, buffer);
    }
}
