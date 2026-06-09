package com.manycoreapis.sdk.core;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Predicate;

public final class PollSupport {
    private PollSupport() {}

    public static <T> T pollUntil(
            PollSupplier<T> supplier,
            Predicate<T> isDone,
            Predicate<T> isFailed,
            Function<T, String> failMessage,
            Duration interval,
            Duration timeout
    ) throws InterruptedException {
        Instant started = Instant.now();
        while (true) {
            T result;
            try {
                result = supplier.get();
            } catch (Exception e) {
                throw new AholoException("Poll step failed", e);
            }
            if (isFailed.test(result)) {
                throw new AholoException(failMessage.apply(result));
            }
            if (isDone.test(result)) {
                return result;
            }
            if (Duration.between(started, Instant.now()).compareTo(timeout) >= 0) {
                throw new AholoException("Polling timed out after " + timeout.toMillis() + "ms");
            }
            Thread.sleep(interval.toMillis());
        }
    }

    @FunctionalInterface
    public interface PollSupplier<T> {
        T get() throws Exception;
    }
}
