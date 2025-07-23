package com.backend.tryal.shared.utils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

// Util Wizard that casts a spell converting unix time to db time
public class TimeWizard {

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    private TimeWizard() {}

    public static LocalDateTime timeSpellconvert(Long unixTime) {
        if (unixTime == null) return null;

        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(unixTime),
                DEFAULT_ZONE
        );
    }
}
