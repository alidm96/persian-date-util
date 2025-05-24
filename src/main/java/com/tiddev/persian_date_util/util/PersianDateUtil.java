package com.tiddev.persian_date_util.util;


import com.tiddev.util.JalaliCalendar;
import com.tiddev.util.mapper.PersianDateMapper.julianToPersian;

import java.time.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static java.time.temporal.ChronoField.*;

/**
 * Utility class for Persian (Jalali) calendar operations, including conversions between Gregorian/Julian
 * and Jalali dates, date arithmetic, formatting, and parsing. Handles timezone adjustments for Asia/Tehran.
 */
public final class PersianDateUtil {

    private static final String DEFAULT_EMPTY_DATE = "000000";
    private static final ZoneId TEHRAN_ZONE = ZoneId.of("Asia/Tehran");
    private static final Locale PERSIAN_LOCALE = Locale.forLanguageTag("fa-IR");
    private static final TimeZone TEHRAN_TIMEZONE = TimeZone.getTimeZone(TEHRAN_ZONE);
    private static final String DATE_REGEX_TEMPLATE = "(?<year>\\d{4})%s(?<month>\\d{2})%s(?<day>\\d{2})"
            + "(?<time>%s(?<hour>\\d{2})%s(?<minute>\\d{2})%s(?<second>\\d{2}))?";

    private PersianDateUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Checks if the year represented by the given temporal is a leap year in the Persian calendar.
     *
     * @param temporal The temporal object containing the date (e.g., {@link LocalDate}).
     * @return {@code true} if the year is a leap year, {@code false} otherwise.
     */
    public static boolean isLeapYear(TemporalAccessor temporal) {
        return JalaliCalendar.isLeepYear(extractYearFromTemporal(temporal));
    }

    /**
     * Checks if the current Persian year is a leap year.
     *
     * @return {@code true} if the current year is a leap year, {@code false} otherwise.
     */
    public static boolean isCurrentYearLeap() {
        return isLeapYear(LocalDate.now());
    }

    /**
     * Formats a {@link LocalDateTime} into a time string with optional separators.
     *
     * @param dateTime     The datetime to format.
     * @param useSeparator If {@code true}, uses "HH:mm:ss"; otherwise, uses "HHmmss".
     * @return Formatted time string.
     */
    public static String formatTime(LocalDateTime dateTime, boolean useSeparator) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(useSeparator ? "HH:mm:ss" : "HHmmss");
        return dateTime.format(formatter);
    }

    /**
     * Converts a Gregorian temporal to a Persian date string using the default separator ("/").
     *
     * @param temporal The temporal to convert (e.g., {@link LocalDateTime}).
     * @return Persian date string in "yyyy/MM/dd" format, or {@code null} if input is {@code null}.
     */
    @julianToPersian
    public static String convertToPersianDate(TemporalAccessor temporal) {
        return convertToPersianDate(temporal, "/");
    }

    /**
     * Converts a Gregorian temporal to a Persian date string with a custom separator.
     *
     * @param temporal  The temporal to convert.
     * @param separator The separator between date components (e.g., "-" for "yyyy-MM-dd").
     * @return Formatted Persian date string, or {@code null} if input is {@code null}.
     */
    public static String convertToPersianDate(TemporalAccessor temporal, String separator) {
        return Optional.ofNullable(temporal)
                .map(PersianDateUtil::extractJalaliDate)
                .map(date -> date.getYearMonthDayBySeprator(separator))
                .orElse(null);
    }

    /**
     * Converts a Gregorian temporal to a compact Persian date string (YYYYMMDD format).
     *
     * @param temporal The temporal to convert.
     * @return Persian date string in "yyyyMMdd" format, or "000000" if input is {@code null}.
     */
    public static String convertToCompactPersianDate(TemporalAccessor temporal) {
        return Optional.ofNullable(temporal)
                .map(t -> extractJalaliDate(adjustForTehranZone(t)))
                .map(date -> date.getYearMonthDayBySeprator(""))
                .orElse(DEFAULT_EMPTY_DATE);
    }

    /**
     * Converts a Gregorian temporal to a Persian datetime string with custom separators.
     *
     * @param temporal          The temporal to convert.
     * @param dateSeparator     Separator for date components (e.g., "/").
     * @param dateTimeSeparator Separator between date and time (e.g., " ").
     * @param timeSeparator     Separator for time components (e.g., ":").
     * @return Formatted datetime string, or "000000" if input is {@code null}.
     */
    public static String convertToPersianDateTime(TemporalAccessor temporal,
                                                  String dateSeparator,
                                                  String dateTimeSeparator,
                                                  String timeSeparator) {
        if (temporal == null) {
            return DEFAULT_EMPTY_DATE;
        }

        JalaliCalendar.DateTime jalaliDateTime = convertToJalaliDateTime(temporal);
        return jalaliDateTime.getDateTimeBySeprator(dateSeparator, dateTimeSeparator, timeSeparator);
    }

    /**
     * Parses a compact Persian date string (YYYYMMDD) to a {@link LocalDateTime} at midnight.
     *
     * @param persianDateString The string to parse (e.g., "14010515" for 1401/05/15).
     * @return Parsed {@link LocalDateTime}, or throws exception for invalid input.
     */
    public static LocalDateTime parseFromCompactString(String persianDateString) {
        int year = Integer.parseInt(persianDateString.substring(0, 4));
        int month = Integer.parseInt(persianDateString.substring(4, 6)) - 1;
        int day = Integer.parseInt(persianDateString.substring(6, 8));

        JalaliCalendar.YearMonthDate gregorianDate =
                JalaliCalendar.jalaliToGregorian(new JalaliCalendar.YearMonthDate(year, month, day));

        return LocalDate.parse(
                gregorianDate.getYearMonthDayBySeprator("/"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        ).atStartOfDay();
    }

    /**
     * Parses a Persian date string with default separators ("/", " ", ":") to a {@link LocalDateTime}.
     *
     * @param persianDateString The string to parse (e.g., "1401/05/15 14:30:00").
     * @return Parsed {@link LocalDateTime}.
     * @throws ParseException If the input format is invalid.
     */
    @julianToPersian
    public static LocalDateTime parseFromString(String persianDateString) throws ParseException {
        return parseFromString(persianDateString, "/", " ", ":");
    }

    /**
     * Parses a Persian date string with custom separators to a {@link LocalDateTime}.
     *
     * @param persianDateString The string to parse.
     * @param dateSeparator     Separator for date components (e.g., "-").
     * @param dateTimeSeparator Separator between date and time (e.g., "T").
     * @param timeSeparator     Separator for time components (e.g., ":").
     * @return Parsed {@link LocalDateTime}, or {@code null} for empty input.
     * @throws ParseException If the input format is invalid.
     */
    public static LocalDateTime parseFromString(String persianDateString,
                                                String dateSeparator,
                                                String dateTimeSeparator,
                                                String timeSeparator) throws ParseException {
        if (persianDateString == null || persianDateString.isEmpty()) {
            return null;
        }

        Matcher matcher = Pattern.compile(
                String.format(DATE_REGEX_TEMPLATE,
                        dateSeparator, dateSeparator,
                        dateTimeSeparator, timeSeparator, timeSeparator)
        ).matcher(persianDateString);

        if (!matcher.matches()) {
            throw new ParseException("Invalid date format: " + persianDateString, 0);
        }

        JalaliCalendar.DateTime jalaliDateTime = extractJalaliDateTimeFromMatcher(matcher);
        JalaliCalendar.DateTime gregorianDateTime = JalaliCalendar.jalaliToGregorian(jalaliDateTime);

        return LocalDateTime.of(
                gregorianDateTime.getYear(),
                gregorianDateTime.getMonth() + 1,
                gregorianDateTime.getDay(),
                gregorianDateTime.getHour(),
                gregorianDateTime.getMinute(),
                gregorianDateTime.getSecond()
        );
    }

    /**
     * Calculates the end of the day (00:00:00 of the next day) for the given temporal.
     *
     * @param temporal The temporal object (e.g., {@link LocalDate} or {@link LocalDateTime}).
     * @return End of the day as {@link LocalDateTime}.
     */
    public static LocalDateTime endOfDay(TemporalAccessor temporal) {
        return toLocalDate(temporal).plusDays(1).atStartOfDay();
    }

    /**
     * Adds months to a {@link LocalDate} using Persian calendar logic.
     *
     * @param date   The base date.
     * @param months Number of months to add (negative values subtract).
     * @return Adjusted date, or {@code null} if input is {@code null}.
     */
    public static LocalDate addMonths(LocalDate date, int months) {
        if (date == null) {
            return null;
        }
        JalaliCalendar jalali = createJalaliCalendar(date.atStartOfDay(TEHRAN_ZONE).toInstant());
        jalali.add(Calendar.MONTH, months);
        return jalali.getTime().toInstant().atZone(TEHRAN_ZONE).toLocalDate();
    }

    /**
     * Adds days to a {@link LocalDate} using Persian calendar logic.
     *
     * @param date The base date.
     * @param days Number of days to add (negative values subtract).
     * @return Adjusted date.
     */
    public static LocalDate addDays(LocalDate date, int days) {
        JalaliCalendar jalali = createJalaliCalendar(date.atStartOfDay(TEHRAN_ZONE).toInstant());
        jalali.add(Calendar.DAY_OF_MONTH, days);
        return Instant.ofEpochMilli(jalali.getTimeInMillis()).atZone(TEHRAN_ZONE).toLocalDate();
    }

    //<editor-fold desc="Helper methods">
    private static JalaliCalendar.YearMonthDate extractJalaliDate(TemporalAccessor temporal) {
        return JalaliCalendar.gregorianToJalali(new JalaliCalendar.YearMonthDate(
                extractYearFromTemporal(temporal),
                extractMonthFromTemporal(temporal) - 1,
                extractDayFromTemporal(temporal)
        ));
    }

    private static JalaliCalendar.DateTime convertToJalaliDateTime(TemporalAccessor temporal) {
        return JalaliCalendar.gregorianToJalali(new JalaliCalendar.DateTime(
                extractYearFromTemporal(temporal),
                extractMonthFromTemporal(temporal) - 1,
                extractDayFromTemporal(temporal),
                extractHourFromTemporal(temporal),
                extractMinuteFromTemporal(temporal),
                extractSecondFromTemporal(temporal)
        ));
    }

    private static TemporalAccessor adjustForTehranZone(TemporalAccessor temporal) {
        if (temporal instanceof LocalDateTime localDateTime) {
            return localDateTime.atZone(TEHRAN_ZONE);
        } else if (temporal instanceof ZonedDateTime zonedDateTime) {
            return zonedDateTime.withZoneSameInstant(TEHRAN_ZONE);
        }
        return temporal;
    }

    private static JalaliCalendar createJalaliCalendar(Instant instant) {
        return new JalaliCalendar(TEHRAN_TIMEZONE, PERSIAN_LOCALE, Date.from(instant));
    }

    private static LocalDate toLocalDate(TemporalAccessor temporal) {
        return (temporal instanceof LocalDateTime localDateTime) ?
                localDateTime.toLocalDate() :
                (LocalDate) temporal;
    }

    //<editor-fold desc="Field extraction helpers">
    private static int extractYearFromTemporal(TemporalAccessor temporal) {
        return temporal.get(YEAR);
    }

    private static int extractMonthFromTemporal(TemporalAccessor temporal) {
        return temporal.get(MONTH_OF_YEAR);
    }

    private static int extractDayFromTemporal(TemporalAccessor temporal) {
        return temporal.get(DAY_OF_MONTH);
    }

    private static int extractHourFromTemporal(TemporalAccessor temporal) {
        return (temporal instanceof LocalDateTime localDateTime) ? localDateTime.getHour() : 0;
    }

    private static int extractMinuteFromTemporal(TemporalAccessor temporal) {
        return (temporal instanceof LocalDateTime localDateTime) ? localDateTime.getMinute() : 0;
    }

    private static int extractSecondFromTemporal(TemporalAccessor temporal) {
        return (temporal instanceof LocalDateTime localDateTime) ? localDateTime.getSecond() : 0;
    }
    //</editor-fold>

    private static JalaliCalendar.DateTime extractJalaliDateTimeFromMatcher(Matcher matcher) {
        return new JalaliCalendar.DateTime(
                Integer.parseInt(matcher.group("year")),
                Integer.parseInt(matcher.group("month")) - 1,
                Integer.parseInt(matcher.group("day")),
                Optional.ofNullable(matcher.group("hour")).map(Integer::parseInt).orElse(0),
                Optional.ofNullable(matcher.group("minute")).map(Integer::parseInt).orElse(0),
                Optional.ofNullable(matcher.group("second")).map(Integer::parseInt).orElse(0)
        );
    }
    //</editor-fold>
}