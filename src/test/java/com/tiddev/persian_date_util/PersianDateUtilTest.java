package com.tiddev.persian_date_util;

import com.tiddev.persian_date_util.util.PersianDateUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link PersianDateUtil}.
 * This class uses JUnit 5 for testing and covers various scenarios for date
 * conversions, formatting, parsing, and arithmetic operations related to
 * the Persian (Jalali) calendar.
 */
class PersianDateUtilTest {

    @ParameterizedTest
    @ValueSource(ints = {1399, 1403, 1408})
    @DisplayName("isLeapYear should correctly identify Persian leap years")
    void testIsLeapYear(int year) {
        LocalDate date = LocalDate.of(year, 1, 1);
        assertTrue(PersianDateUtil.isLeapYear(date), "Year " + year + " should be a leap year");
    }

    @ParameterizedTest
    @ValueSource(ints = {1400, 1401, 1402, 1404})
    @DisplayName("isLeapYear should correctly identify non-leap years")
    void testIsNotLeapYear(int year) {
        LocalDate date = LocalDate.of(year, 1, 1);
        assertFalse(PersianDateUtil.isLeapYear(date), "Year " + year + " should not be a leap year");
    }

    @Test
    @DisplayName("isCurrentYearLeap should return correct leap year status for current year")
    void testIsCurrentYearLeap() {
        assertEquals(PersianDateUtil.isCurrentYearLeap(), PersianDateUtil.isLeapYear(LocalDate.now()));
    }

    @ParameterizedTest
    @CsvSource({
            "2023-01-15T10:30:45, true, 10:30:45",
            "2023-01-15T05:05:05, false, 050505",
            "2023-12-31T23:59:59, true, 23:59:59"
    })
    @DisplayName("formatTime should format LocalDateTime to time string correctly")
    void testFormatTime(LocalDateTime dateTime, boolean useSeparator, String expected) {
        assertEquals(expected, PersianDateUtil.formatTime(dateTime, useSeparator));
    }

    @Test
    @DisplayName("convertToPersianDate should convert Gregorian to Persian date with default separator")
    void testConvertToPersianDate_defaultSeparator() {
        LocalDateTime dateTime = LocalDateTime.of(2023, Month.MAY, 15, 10, 0, 0);
        assertEquals("1402/02/25", PersianDateUtil.convertToPersianDate(dateTime));
        assertNull(PersianDateUtil.convertToPersianDate(null));
    }

    @ParameterizedTest
    @CsvSource({
            "2023-05-15T10:00:00, -, 1402-02-25",
            "2024-03-20T10:00:00, ., 1403.01.01",
            "2024-03-21T10:00:00, *, 1403*01*02"
    })
    @DisplayName("convertToPersianDate should convert Gregorian to Persian date with custom separator")
    void testConvertToPersianDate_customSeparator(LocalDateTime dateTime, String separator, String expected) {
        assertEquals(expected, PersianDateUtil.convertToPersianDate(dateTime, separator));
    }

    @Test
    @DisplayName("convertToCompactPersianDate should convert Gregorian to compact Persian date")
    void testConvertToCompactPersianDate() {
        LocalDateTime dateTime = LocalDateTime.of(2023, Month.MAY, 15, 10, 0, 0);
        assertEquals("14020225", PersianDateUtil.convertToCompactPersianDate(dateTime));
        assertEquals("000000", PersianDateUtil.convertToCompactPersianDate(null));
    }

    @ParameterizedTest
    @CsvSource({
            "2023-05-15T10:30:45, /, ' ', :, 1402/02/25 10:30:45",
            "2024-01-01T00:00:00, -, T, ., 1402-10-11T00.00.00",
            "2024-03-21T14:15:00, _, _, _, 1403_01_02_14_15_00"
    })
    @DisplayName("convertToPersianDateTime should convert Gregorian to Persian datetime with custom separators")
    void testConvertToPersianDateTime(LocalDateTime dateTime,
                                      String dateSep,
                                      String dateTimeSep,
                                      String timeSep,
                                      String expected) {
        assertEquals(expected, PersianDateUtil.convertToPersianDateTime(dateTime, dateSep, dateTimeSep, timeSep));
        assertEquals(
                "000000",
                PersianDateUtil.convertToPersianDateTime(
                        null,
                        "/",
                        " ",
                        ":"
                )
        );
    }

    @Test
    @DisplayName("parseFromCompactString should parse compact Persian date to LocalDateTime")
    void testParseFromCompactString() {
        String persianDateString = "14020225";
        LocalDateTime expectedDateTime =
                LocalDateTime.of(2023, Month.MAY, 15, 0, 0, 0);
        assertEquals(expectedDateTime, PersianDateUtil.parseFromCompactString(persianDateString));

        String leapYearDate = "14030101";
        LocalDateTime expectedLeapYearDateTime =
                LocalDateTime.of(2024, Month.MARCH, 20, 0, 0, 0);
        assertEquals(expectedLeapYearDateTime, PersianDateUtil.parseFromCompactString(leapYearDate));
    }

    @Test
    @DisplayName("parseFromString should parse Persian datetime string with default separators")
    void testParseFromString_defaultSeparators() throws ParseException {
        String persianDateTimeString = "1402/02/25 10:30:45";
        LocalDateTime expectedDateTime =
                LocalDateTime.of(2023, Month.MAY, 15, 10, 30, 45);
        assertEquals(expectedDateTime, PersianDateUtil.parseFromString(persianDateTimeString));

        String persianDateOnlyString = "1402/02/25";
        LocalDateTime expectedDateOnly =
                LocalDateTime.of(2023, Month.MAY, 15, 0, 0, 0);
        assertEquals(expectedDateOnly, PersianDateUtil.parseFromString(persianDateOnlyString));

        assertNull(PersianDateUtil.parseFromString(""));
        assertNull(PersianDateUtil.parseFromString(null));
    }

    @ParameterizedTest
    @CsvSource({
            "1402-02-25T10.30.45, -, T, ., 2023-05-15T10:30:45",
            "1403_01_01_00_00_00, _, _, _, 2024-03-20T00:00:00"
    })
    @DisplayName("parseFromString should parse Persian datetime string with custom separators")
    void testParseFromString_customSeparators(String persianDateTimeString,
                                              String dateSep,
                                              String dateTimeSep,
                                              String timeSep,
                                              LocalDateTime expected) throws ParseException {
        assertEquals(expected, PersianDateUtil.parseFromString(persianDateTimeString, dateSep, dateTimeSep, timeSep));
    }

    @Test
    @DisplayName("parseFromString should throw ParseException for invalid format")
    void testParseFromString_invalidFormat() {
        assertThrows(ParseException.class,
                () -> PersianDateUtil.parseFromString(
                        "1402-02-25 10:30",
                        "/",
                        " ",
                        ":"
                ),
                "Should throw ParseException for invalid time format");
        assertThrows(ParseException.class,
                () -> PersianDateUtil.parseFromString(
                        "invalid_date",
                        "/",
                        " ",
                        ":"
                ),
                "Should throw ParseException for completely invalid string");
    }

    @ParameterizedTest
    @CsvSource({
            "2023-05-15T10:30:45, 2023-05-16T00:00:00",
            "2023-12-31T23:59:59, 2024-01-01T00:00:00",
            "2024-03-20T00:00:00, 2024-03-21T00:00:00"
    })
    @DisplayName("endOfDay should return the start of the next day")
    void testEndOfDay(LocalDateTime inputDateTime, LocalDateTime expectedEndOfDay) {
        assertEquals(expectedEndOfDay, PersianDateUtil.endOfDay(inputDateTime));
        assertEquals(expectedEndOfDay, PersianDateUtil.endOfDay(inputDateTime.toLocalDate()));
    }

    @Test
    @DisplayName("addMonths should add months correctly to a LocalDate")
    void testAddMonths() {
        LocalDate baseDate = LocalDate.of(2023, Month.JANUARY, 1);
        LocalDate expectedDate = LocalDate.of(2023, Month.MARCH, 2);
        assertEquals(expectedDate, PersianDateUtil.addMonths(baseDate, 2));

        baseDate = LocalDate.of(2023, Month.MAY, 15);
        expectedDate = LocalDate.of(2024, Month.MAY, 14);
        assertEquals(expectedDate, PersianDateUtil.addMonths(baseDate, 12));

        baseDate = LocalDate.of(2023, Month.MAY, 15);
        expectedDate = LocalDate.of(2023, Month.MARCH, 16);
        assertEquals(expectedDate, PersianDateUtil.addMonths(baseDate, -2));

        assertNull(PersianDateUtil.addMonths(null, 5));
    }

    @Test
    @DisplayName("addDays should add days correctly to a LocalDate")
    void testAddDays() {
        LocalDate baseDate = LocalDate.of(2023, Month.MAY, 15);

        LocalDate expectedDate = LocalDate.of(2023, Month.MAY, 20);
        assertEquals(expectedDate, PersianDateUtil.addDays(baseDate, 5));

        baseDate = LocalDate.of(2023, Month.MAY, 25);
        expectedDate = LocalDate.of(2023, Month.JUNE, 4);
        assertEquals(expectedDate, PersianDateUtil.addDays(baseDate, 10));

        baseDate = LocalDate.of(2023, Month.MAY, 15);
        expectedDate = LocalDate.of(2023, Month.MAY, 10);
        assertEquals(expectedDate, PersianDateUtil.addDays(baseDate, -5));

        baseDate = LocalDate.of(2024, Month.MARCH, 19);
        expectedDate = LocalDate.of(2024, Month.MARCH, 20);
        assertEquals(expectedDate, PersianDateUtil.addDays(baseDate, 1));

        baseDate = LocalDate.of(2024, Month.MARCH, 20);
        expectedDate = LocalDate.of(2024, Month.MARCH, 21);
        assertEquals(expectedDate, PersianDateUtil.addDays(baseDate, 1));

        baseDate = LocalDate.of(2024, Month.APRIL, 1);
        expectedDate = LocalDate.of(2024, Month.APRIL, 6);
        assertEquals(expectedDate, PersianDateUtil.addDays(baseDate, 5));
    }
}
