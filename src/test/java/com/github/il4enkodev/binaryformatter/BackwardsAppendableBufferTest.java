package com.github.il4enkodev.binaryformatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("A BackwardsAppendableBuffer")
class BackwardsAppendableBufferTest {

    BackwardsAppendableBuffer buffer;

    @Test
    @DisplayName("is instantiated with default constructor")
    void constructDefault() {
        assertDoesNotThrow(() -> new BackwardsAppendableBuffer());
    }

    @Test
    @DisplayName("is instantiated with initial capacity")
    void constructWithInitialCapacity() {
        assertDoesNotThrow(() -> new BackwardsAppendableBuffer(8));
        assertDoesNotThrow(() -> new BackwardsAppendableBuffer(0));
    }

    @Test
    @DisplayName("throws IllegalArgumentException when creates with a negative initial capacity")
    void constructWithNegativeInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new BackwardsAppendableBuffer(-1));
    }

    @Nested
    @DisplayName("corresponds to the Appendable contract")
    class AppendableContract {

        @BeforeEach
        void createBuffer() {
            buffer = new BackwardsAppendableBuffer();
        }

        @Test
        @DisplayName("is be able to append char sequence that is null or empty")
        void appendSequenceThatIsNull() {
            assertDoesNotThrow(() -> buffer.append(null));
            assertDoesNotThrow(() -> buffer.append(""));
        }

        @Test
        @DisplayName("is be able to append subsequence that is null or empty")
        void appendSubsequenceThatIsNull() {
            assertDoesNotThrow(() -> buffer.append(null, 0, 3));
            assertDoesNotThrow(() -> buffer.append("", 0, 0));
        }

        @ParameterizedTest(name = "CharSequence: {0}, start: {1}, end: {2}")
        @CsvSource({"abc, -1, 3", "abc, 3, 1", "abc, 0, 4"})
        @DisplayName("throws IndexOutOfBoundsException when appends char sequence with invalid range")
        void appendSubsequenceWithInvalidRange(String csq, int start, int end) {
            assertThrows(IndexOutOfBoundsException.class, () -> buffer.append(csq, start, end));
        }

        @Test
        @DisplayName("returns the same buffer on each append call")
        void returnsTheSameBufferOnEachAppendCall() {
            assertSame(buffer, buffer.append('a'));
            assertSame(buffer, buffer.append("abc"));
            assertSame(buffer, buffer.append("abc", 0, 3));
        }
    }

    @Nested
    @DisplayName("when new")
    class WhenNew extends CharSequenceContract {

        int initialCapacity = 8;

        @BeforeEach
        void createBuffer() {
            buffer = new BackwardsAppendableBuffer(initialCapacity);
        }

        @Test
        @DisplayName("should have capacity equals to initialCapacity")
        void capacity() {
            assertEquals(initialCapacity, buffer.capacity());
        }

        @Override
        public CharSequence testingEntity() {
            return buffer;
        }

        @Override
        public String expectedContent() {
            return "";
        }

        @Nested
        @DisplayName("after single string appended")
        class AppendSingleString extends CharSequenceContract {

            private final String appendedString = "abc";

            @BeforeEach
            void appendString() {
                buffer.append(appendedString);
            }

            @Override
            public CharSequence testingEntity() {
                return buffer;
            }

            @Override
            public String expectedContent() {
                return appendedString;
            }
        }

        @Nested
        @DisplayName("after append string that exceed capacity")
        class AppendLongString {

            String stringToAppend;

            @BeforeEach
            void buildString() {
                final StringBuilder builder = new StringBuilder();
                for (int i = 0; i < initialCapacity + 1; i++)
                    builder.append(i);
                stringToAppend = builder.toString();
            }

            @Test
            @DisplayName("should expand")
            void isExpand() {
                assertTrue(stringToAppend.length() > initialCapacity);
                assertDoesNotThrow(() -> buffer.append(stringToAppend));
                assertTrue(buffer.capacity() > initialCapacity);
            }
        }

        @Nested
        @DisplayName("after multiple chars appended")
        class AfterMultipleCharsAppended extends CharSequenceContract {

            String charsToAppend = "abc";
            String expectedContent = "cba";

            @BeforeEach
            void appendChars() {
                for (int i = 0; i < charsToAppend.length(); i++)
                    buffer.append(charsToAppend.charAt(i));
            }

            @Override
            public String expectedContent() {
                return expectedContent;
            }

            @Override
            public CharSequence testingEntity() {
                return buffer;
            }

            @Test
            @Override
            @DisplayName("contains appended chars in backwards order")
            public void toStringTest() {
                super.toStringTest();
            }
        }

        @Nested
        @DisplayName("after multiple strings appended")
        class AfterMultipleStringAppended extends CharSequenceContract {

            String[] stringsToAppend = {"abc", "def", "ghi"};
            String expectedContent = "ghidefabc";

            @BeforeEach
            void appendStrings() {
                for (String s : stringsToAppend)
                    buffer.append(s);
            }

            @Override
            public CharSequence testingEntity() {
                return buffer;
            }

            @Override
            public String expectedContent() {
                return expectedContent;
            }

            @Test
            @Override
            @DisplayName("contains appended strings in backwards order")
            public void toStringTest() {
                super.toStringTest();
            }
        }

        @Nested
        @DisplayName("when buffer isn't empty")
        class AfterReset {

            @BeforeEach
            void populateBuffer() {
                buffer.append("some data");
            }

            @Test
            @DisplayName("reset() returns the same buffer that is empty")
            void resetReturnsTheSameBuffer() {
                assertSame(buffer, buffer.reset());
                assertEquals(0, buffer.length());
                assertEquals("", buffer.toString());
            }

        }


    }


}