package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.exception.LastMailRecentlySendException;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Util_General {

    public static void verifyLastMailSentAtSeconds(LocalDateTime lastMailSentAtSeconds)
            throws LastMailRecentlySendException {
        if (lastMailSentAtSeconds != null) {
            long seconds = Duration.between(lastMailSentAtSeconds, LocalDateTime.now()).getSeconds();
            if (seconds < 60) {
                throw new LastMailRecentlySendException(HttpStatus.TOO_MANY_REQUESTS, 60 - seconds,
                                                        "Last mail has been send recently.");
            }
        }
    }
}
