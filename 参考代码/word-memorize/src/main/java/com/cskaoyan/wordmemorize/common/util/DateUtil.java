package com.cskaoyan.wordmemorize.common.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    private static final String DEFAULT_TIME_FORMATTER = "HH:mm";

    public static LocalDate getDate(String dateStr) {
        return getDate(dateStr, DEFAULT_DATE_PATTERN);
    }

    public static LocalDate getDate(String dateStr, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return LocalDate.parse(dateStr, formatter);
    }

    public static LocalTime getTime(String timeStr) {
        return getTime(timeStr, DEFAULT_TIME_FORMATTER);
    }

    public static LocalTime getTime(String timeStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalTime.parse(timeStr, dateTimeFormatter);
    }
}
