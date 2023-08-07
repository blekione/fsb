package com.blekione.fsb.test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestUtils {
    private static final Instant CLOCK_INSTANT = Instant.parse("2023-08-05T10:20:30.000Z");
    private static final Clock CLOCK = Clock.fixed(CLOCK_INSTANT, ZoneId.systemDefault());
    private static final LocalDateTime FIXED_NOW_TIME = LocalDateTime.now(CLOCK);

    private TestUtils() {
    }

    /**
     * @return a fixed clock instant to the timestamp 20323-08-05T10:20:30
     * for testing purpose
     */
    public static Clock getFixedClock() {
        return CLOCK;
    }

    /**
     * @return a fixed {@link LocalDateTime#now()} time of 20323-08-05T10:20:30
     * for testing purpose
     */
    public static LocalDateTime getFixedNowTime() {
        return FIXED_NOW_TIME;
    }
}
