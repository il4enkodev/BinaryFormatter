package com.github.il4enkodev.binaryformatter;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNegative;
import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNull;

public abstract class Format {

    public enum Alignment {
        LEFT, RIGHT
    }

    int groupSize;
    private int minWidth = 0;
    private CharSequence prefix = "";
    private CharSequence suffix = "";
    private char groupingSeparator;
    private Alignment alignment = Alignment.RIGHT;

    final char zero;

    char space = ' ';

    /*package-private*/ Format() {
        this(Locale.getDefault());
    }

    /*package-private*/ Format(Locale locale) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        zero = symbols.getZeroDigit();
        groupingSeparator = symbols.getGroupingSeparator();
    }

    public Format groupSize(int groupSize) {
        this.groupSize = Preconditions.verifyPositive(groupSize, "groupSize");
        return this;
    }

    public int groupSize() {
        return groupSize;
    }

    public Format groupingSeparator(char groupingSeparator) {
        this.groupingSeparator = groupingSeparator;
        return this;
    }

    public char groupingSeparator() {
        return groupingSeparator;
    }

    public Format minWidth(int minWidth) {
        this.minWidth = verifyNonNegative(minWidth, "minWidth");
        return this;
    }

    public Format prefix(CharSequence prefix) {
        this.prefix = prefix != null ? prefix : "";
        return this;
    }

    public Format suffix(CharSequence suffix) {
        this.suffix = suffix != null ? suffix : "";
        return this;
    }

    public Format alignment(Alignment alignment) {
        this.alignment = verifyNonNull(alignment, "alignment");
        return this;
    }

    public final int minWidth() {
        return minWidth;
    }

    public final CharSequence prefix() {
        return prefix;
    }

    public final CharSequence suffix() {
        return suffix;
    }

    public final Alignment alignment() {
        return alignment;
    }

    final int contentWidth(int digitsCount) {
        int w = digitsCount;
        if (groupSize > 0)
            while ((digitsCount -= groupSize) > 0)
                ++w;
        return w + prefix.length() + suffix.length();
    }

    void writeSuffixAndSpaces(int spacesCount, BackwardsAppendableBuffer buffer) {
        if (Alignment.LEFT == alignment)
            writeSpaces(spacesCount, buffer);
        buffer.append(suffix);
    }

    void writePrefixAndSpaces(int spacesCount, BackwardsAppendableBuffer buffer) {
        buffer.append(prefix);
        if (Alignment.RIGHT == alignment)
            writeSpaces(spacesCount, buffer);
    }

    void writeSpaces(int count, BackwardsAppendableBuffer buffer) {
        for ( ; count > 0; --count)
            buffer.append(space);
    }

}
