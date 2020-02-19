package com.github.il4enkodev.binaryformatter;

public final class Preconditions {

    public static int verifyPositive(int value, String name) {
        if (value <= 0) throw new IllegalArgumentException(name + " == " + value);
        else return value;
    }

    public static int verifyNonNegative(int value, String name) {
        if (value < 0) throw new IllegalArgumentException(name + " == " + value);
        else return value;
    }

    public static <T> T verifyNonNull(T value, String name) {
        if (value == null) throw new NullPointerException(name + " is null");
        else return value;
    }

    private Preconditions() {
        // No instance
    }
}
