package com.sbi.example.util;

import java.security.SecureRandom;

public class RandomNumberGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static long generate12DigitNumber() {
        // 12-digit numbers start at 100000000000 and end at 999999999999
        long min = 100_000_000_000L;
        long max = 999_999_999_999L;
        return min + ((long) (random.nextDouble() * (max - min)));
    }
}
