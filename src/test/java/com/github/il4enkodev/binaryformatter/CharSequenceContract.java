package com.github.il4enkodev.binaryformatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class CharSequenceContract {

    @Test
    @DisplayName("length() corresponds to the CharSequence contract")
    void length() {
        assertEquals(expectedContent().length(), testingEntity().length());
    }

    @Test
    @DisplayName("charAt() returns correct values for valid indexes")
    void charAtForValidIndexes() {
        final char[] expectedChars = expectedContent().toCharArray();
        final CharSequence testingEntity = testingEntity();

        for (int i = 0; i < expectedChars.length; i++) {
            assertEquals(expectedChars[i], testingEntity.charAt(i), "Failed for index: " + i);
        }
    }

    @Test
    @DisplayName("charAt() throws IndexOutOfBoundsException for invalid indexes")
    void charAtForInvalidIndexes() {
        final CharSequence testingEntity = testingEntity();
        final String expectedContent = expectedContent();

        assertThrows(IndexOutOfBoundsException.class, () -> testingEntity.charAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> testingEntity.charAt(expectedContent.length()));
    }

    @Test
    @DisplayName("subSequence() return correct sequence for valid range")
    void subSequenceForValidParams() {
        final CharSequence testingEntity = testingEntity();
        final String expectedContent = expectedContent();

        assertEquals(expectedContent, testingEntity.subSequence(0, expectedContent.length()).toString());

        if (expectedContent.length() > 1) {
            assertEquals(expectedContent.subSequence(0, 1).toString(), testingEntity.subSequence(0, 1).toString());
            assertEquals(expectedContent.subSequence(1, 2).toString(), testingEntity.subSequence(1, 2).toString());
        }
    }

    @Test
    @DisplayName("subSequence() throws IndexOutOfBoundsException for invalid range")
    void subSequenceForInvalidParams() {
        final CharSequence testingEntity = testingEntity();
        final String expectedContent = expectedContent();

        assertThrows(IndexOutOfBoundsException.class, () -> testingEntity.subSequence(-1, expectedContent.length()));
        assertThrows(IndexOutOfBoundsException.class, () -> testingEntity.subSequence(0, expectedContent.length() + 1));

        if (expectedContent.length() > 0) {
            assertThrows(IndexOutOfBoundsException.class, () -> testingEntity.subSequence(expectedContent.length(), 0));
        }
    }

    @Test
    @DisplayName("toSting() corresponds to the CharSequence contract")
    void toStringTest() {
        assertEquals(expectedContent(), testingEntity().toString());
    }

    abstract CharSequence testingEntity();

    abstract String expectedContent();


}
