package com.urlShortener.util;

import java.util.concurrent.atomic.AtomicLong;

public class SnowflakeIdGenerator {
    private static final long EPOCH = 1640995200000L;
    private static final long MACHINE_ID = 1;
    private static final long SEQUENCE_BITS = 12;
    private static final long MACHINE_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = MACHINE_SHIFT + 5;
    private static final AtomicLong lastTimestamp = new AtomicLong(-1);
    private static final AtomicLong sequence = new AtomicLong(0);

    public static synchronized long generateId() {
        long timestamp = System.currentTimeMillis() - EPOCH;

        if (timestamp == lastTimestamp.get()) {
            sequence.incrementAndGet();
            if (sequence.get() >= (1 << SEQUENCE_BITS)) {
                while (timestamp <= lastTimestamp.get()) {
                    timestamp = System.currentTimeMillis() - EPOCH;
                }
                sequence.set(0);
            }
        } else {
            sequence.set(0);
        }

        lastTimestamp.set(timestamp);
        return (timestamp << TIMESTAMP_SHIFT) | (MACHINE_ID << MACHINE_SHIFT) | sequence.get();
    }
}
