package com.ty_yak.auth.util;

import lombok.experimental.UtilityClass;

import java.time.*;

@UtilityClass
public class DateUtil {

    public static LocalDate getLocalDateNow() {
        return LocalDate.now();
    }

    public static LocalDate getLocalDateNowAtTimezone(ZoneId zoneId) {
        return LocalDate.now(zoneId);
    }

    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }

    public static LocalTime getLocalTimeNow() {
        return LocalTime.now();
    }

    public static long getCurrentTimeInMilliseconds() {
        return System.currentTimeMillis();
    }

    public static ZonedDateTime getStartOfADayWithTimezone(ZoneId zoneId) {
        return ZonedDateTime.now(zoneId).toLocalDate().atStartOfDay(zoneId).withEarlierOffsetAtOverlap();
    }

    public static ZonedDateTime getStartOfAGivenDayWithTimezone(ZoneId zoneId, ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(zoneId).toLocalDate().atStartOfDay(zoneId).withEarlierOffsetAtOverlap();
    }

    public static ZonedDateTime getZoneDateTimeNow(String timezone) {
        return ZonedDateTime.now(ZoneId.of(timezone));
    }

    public static String convertSecondsToUsefulFormat(Long seconds) {
        long sec = seconds % 60;
        long min = (seconds / 60) % 60;
        long hours = seconds / 3600;

        String strSec = (sec < 10) ? "0" + sec : Long.toString(sec);
        String strMin = (min < 10) ? "0" + min : Long.toString(min);
        String strHours = (hours < 10) ? "0" + hours : Long.toString(hours);

        return strHours + ":" + strMin + ":" + strSec;
    }
}
