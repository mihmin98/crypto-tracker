package com.mihmin98.cryptotrackerbackend.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    public static final long MILLIS_IN_A_DAY = 86_400_000;

    public static long computeDaysBetweenDateMillis(long startTimeMillis, long endTimeMillis) {
        if (endTimeMillis < startTimeMillis) {
            throw new IllegalArgumentException("endTimeMillis cannot be smaller than startTimeMillis");
        }

        ZonedDateTime startDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startTimeMillis), ZoneId.of("UTC"));
        ZonedDateTime endDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(endTimeMillis), ZoneId.of("UTC"));

        return ChronoUnit.DAYS.between(startDateTime, endDateTime);
    }

    public static ZonedDateTime convertUTCEpochMillisToZonedDateTime(long epochMillis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of("UTC"));
    }

    public static long convertUTCEpochMillisToStartOfDay(long epochMillis) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.of("UTC"));
        return zonedDateTime.withHour(0).withMinute(0).withSecond(0).toInstant().toEpochMilli();
    }

    public static ZonedDateTime convertUTCDateToZonedDateTime(String dateUTC) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return ZonedDateTime.parse(dateUTC, dateTimeFormatter);
    }

    public static long convertUTCDateToEpochMillis(String dateUTC, boolean setAtBeginningOfDay) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateUTC, dateTimeFormatter);

        if (setAtBeginningOfDay) {
            zonedDateTime = zonedDateTime.withHour(0).withMinute(0).withSecond(0);
        }

        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static long convertUTCDateToEpochMillis(String dateUTC) {
        return convertUTCDateToEpochMillis(dateUTC, false);
    }
}
