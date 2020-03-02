package com.github.il4enkodev.binaryformatter;

import static com.github.il4enkodev.binaryformatter.Preconditions.verifyNonNegative;

final class BackwardsAppendableBuffer implements Appendable, CharSequence {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    static final int DEFAULT_CAPACITY = 16;

    private char[] chars;
    private int position;

    BackwardsAppendableBuffer() {
        this(DEFAULT_CAPACITY);
    }

    BackwardsAppendableBuffer(int initialCapacity) {
        verifyNonNegative(initialCapacity, "initialCapacity");
        chars = new char[initialCapacity];
        position = initialCapacity;
    }

    int capacity() {
        return chars.length;
    }

    BackwardsAppendableBuffer reset() {
        position = chars.length;
        return this;
    }

    @Override
    public int length() {
        return chars.length - position;
    }

    @Override
    public char charAt(int index) {
        final int length = length();
        if (index < 0 || index >= length)
            throw new IndexOutOfBoundsException("index: " + index + ", length: " + length);
        return chars[position + index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || start > end || end > length())
            throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + ", length: " + length());
        int offset = position + start;
        int count = end - start;
        return new String(chars, offset, count);
    }

    @Override
    public String toString() {
        final int length = length();
        return length == 0 ? "" : new String(chars, position, length);
    }

    @Override
    public BackwardsAppendableBuffer append(CharSequence csq) {
        if (csq == null)
            csq = "null";
        return append(csq, 0, csq.length());
    }

    @Override
    public BackwardsAppendableBuffer append(CharSequence csq, int start, int end) {
        if (csq == null)
            csq = "null";

        if (start < 0 || start > end || start + end > csq.length()) {
            throw new IndexOutOfBoundsException(
                    "csq.length(): " + csq.length() + ", start: " + start + ", end: " + end);
        }

        final int length = end - start;
        if (length != 0) {
            ensureWritable(length);
            for (int i = end - 1; i >= start; --i)
                chars[--position] = csq.charAt(i);
        }
        return this;
    }

    @Override
    public BackwardsAppendableBuffer append(char c) {
        ensureWritable(1);
        chars[--position] = c;
        return this;
    }

    private void ensureWritable(int size) {
        if (size > position)
            expand(newCapacity(chars.length + size));
    }

    private void expand(int newCapacity) {
        char[] newArray = new char[newCapacity];
        final int newPosition = newCapacity - length();
        System.arraycopy(chars, position, newArray, newPosition, length());
        chars = newArray;
        position = newPosition;
    }

    private int newCapacity(int minCapacity) {
        // overflow-conscious code
        int newCapacity = (chars.length << 1) + 2;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
                ? hugeCapacity(minCapacity)
                : newCapacity;
    }

    private int hugeCapacity(int minCapacity) {
        if (Integer.MAX_VALUE - minCapacity < 0) { // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? minCapacity : MAX_ARRAY_SIZE;
    }
}
