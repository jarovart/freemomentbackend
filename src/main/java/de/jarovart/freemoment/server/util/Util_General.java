package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.exception.LastMailRecentlySendException;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Util_General {
    public static final int COOLDOWN_IN_SECONDS = 60;

    private Util_General() {
    }

    public static void verifyLastMailSentAtSeconds(LocalDateTime lastMailSentAtSeconds)
            throws LastMailRecentlySendException {
        if (lastMailSentAtSeconds != null) {
            long seconds = Duration.between(lastMailSentAtSeconds, LocalDateTime.now()).getSeconds();
            if (seconds < COOLDOWN_IN_SECONDS) {
                throw new LastMailRecentlySendException(HttpStatus.TOO_MANY_REQUESTS, COOLDOWN_IN_SECONDS - seconds,
                                                        "Last mail has been send recently.");
            }
        }
    }
}
