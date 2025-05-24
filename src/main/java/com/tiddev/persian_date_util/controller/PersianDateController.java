package com.tiddev.persian_date_util.controller;

import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import com.tiddev.persian_date_util.util.PersianDateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.text.ParseException;

/**
 * REST Controller for exposing Persian (Jalali) date utility operations.
 * This controller provides endpoints for date conversions, formatting, parsing,
 * and arithmetic operations related to the Persian calendar, leveraging the
 * {@link PersianDateUtil} class.
 */
@RestController
@RequestMapping("/api/persian-date")
@Tag(name = "Persian Date Operations",
        description = """
                Utility for Persian (Jalali) calendar operations,
                including conversions, formatting, parsing, and arithmetic.
                """
)
public class PersianDateController {

    /**
     * Checks if the year represented by the given Gregorian date is a leap year in the Persian calendar.
     *
     * @param gregorianDate The Gregorian date to check (e.g., 2024-03-20 for 1403/01/01).
     * @return {@code true} if the year is a Persian leap year, {@code false} otherwise.
     */
    @Operation(summary = "Check if a Gregorian year corresponds to a Persian leap year")
    @GetMapping("/is-leap-year")
    public ResponseEntity<Boolean> isLeapYear(
            @Parameter(description = """
            Gregorian date to check, e.g., 2024-03-20 (which is 1403/01/01 in Persian calendar).
            """)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate gregorianDate) {
        return ResponseEntity.ok(PersianDateUtil.isLeapYear(gregorianDate));
    }

    /**
     * Checks if the current Persian year is a leap year.
     *
     * @return {@code true} if the current year is a Persian leap year, {@code false} otherwise.
     */
    @Operation(summary = "Check if the current Persian year is a leap year")
    @GetMapping("/is-current-year-leap")
    public ResponseEntity<Boolean> isCurrentYearLeap() {
        return ResponseEntity.ok(PersianDateUtil.isCurrentYearLeap());
    }

    /**
     * Formats a {@link LocalDateTime} into a time string with optional separators.
     *
     * @param dateTime     The Gregorian datetime to format.
     * @param useSeparator If {@code true}, uses "HH:mm:ss"; otherwise, uses "HHmmss".
     * @return Formatted time string.
     */
    @Operation(summary = "Format a Gregorian LocalDateTime into a time string")
    @GetMapping("/format-time")
    public ResponseEntity<String> formatTime(
            @Parameter(description = "Gregorian date and time to format, e.g., 2023-01-15T10:30:45")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
            @Parameter(description = "Use separator (true for HH:mm:ss, false for HHmmss)")
            @RequestParam(defaultValue = "true") boolean useSeparator) {
        return ResponseEntity.ok(PersianDateUtil.formatTime(dateTime, useSeparator));
    }

    /**
     * Converts a Gregorian temporal to a Persian date string using the default separator ("/").
     *
     * @param gregorianTemporal The Gregorian temporal to convert (e.g., {@link LocalDateTime}).
     * @return Persian date string in "yyyy/MM/dd" format.
     */
    @Operation(summary = "Convert Gregorian date to Persian date string with default separator")
    @GetMapping("/convert-to-persian-date-default")
    public ResponseEntity<String> convertToPersianDateDefault(
            @Parameter(description = "Gregorian date and time to convert, e.g., 2023-05-15T10:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime gregorianTemporal) {
        return ResponseEntity.ok(PersianDateUtil.convertToPersianDate(gregorianTemporal));
    }

    /**
     * Converts a Gregorian temporal to a Persian date string with a custom separator.
     *
     * @param gregorianTemporal The Gregorian temporal to convert.
     * @param separator         The separator between date components (e.g., "-" for "yyyy-MM-dd").
     * @return Formatted Persian date string.
     */
    @Operation(summary = "Convert Gregorian date to Persian date string with custom separator")
    @GetMapping("/convert-to-persian-date-custom")
    public ResponseEntity<String> convertToPersianDateCustom(
            @Parameter(description = "Gregorian date and time to convert, e.g., 2023-05-15T10:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime gregorianTemporal,
            @Parameter(description = "Separator for date components (e.g., -)", example = "-")
            @RequestParam(defaultValue = "/") String separator) {
        return ResponseEntity.ok(PersianDateUtil.convertToPersianDate(gregorianTemporal, separator));
    }

    /**
     * Converts a Gregorian temporal to a compact Persian date string (YYYYMMDD format).
     *
     * @param gregorianTemporal The Gregorian temporal to convert.
     * @return Persian date string in "yyyyMMdd" format, or "000000" if input is {@code null}.
     */
    @Operation(summary = "Convert Gregorian date to compact Persian date string (YYYYMMDD)")
    @GetMapping("/convert-to-compact-persian-date")
    public ResponseEntity<String> convertToCompactPersianDate(
            @Parameter(description = "Gregorian date and time to convert, e.g., 2023-05-15T10:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime gregorianTemporal) {
        return ResponseEntity.ok(PersianDateUtil.convertToCompactPersianDate(gregorianTemporal));
    }

    /**
     * Converts a Gregorian temporal to a Persian datetime string with custom separators.
     *
     * @param gregorianTemporal The Gregorian temporal to convert.
     * @param dateSeparator     Separator for date components (e.g., "/").
     * @param dateTimeSeparator Separator between date and time (e.g., " ").
     * @param timeSeparator     Separator for time components (e.g., ":").
     * @return Formatted datetime string.
     */
    @Operation(summary = "Convert Gregorian date to Persian datetime string with custom separators")
    @GetMapping("/convert-to-persian-datetime")
    public ResponseEntity<String> convertToPersianDateTime(
            @Parameter(description = "Gregorian date and time to convert, e.g., 2023-05-15T10:30:45")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime gregorianTemporal,
            @Parameter(description = "Separator for date components (e.g., /)", example = "/")
            @RequestParam(defaultValue = "/") String dateSeparator,
            @Parameter(description = "Separator between date and time (e.g., space)", example = " ")
            @RequestParam(defaultValue = " ") String dateTimeSeparator,
            @Parameter(description = "Separator for time components (e.g., :)", example = ":")
            @RequestParam(defaultValue = ":") String timeSeparator) {
        return ResponseEntity.ok(
                PersianDateUtil.convertToPersianDateTime(
                        gregorianTemporal,
                        dateSeparator,
                        dateTimeSeparator,
                        timeSeparator
                )
        );
    }

    /**
     * Parses a compact Persian date string (YYYYMMDD) to a {@link LocalDateTime} at midnight.
     *
     * @param persianDateString The string to parse (e.g., "14010515" for 1401/05/15).
     * @return Parsed {@link LocalDateTime}.
     * @throws ResponseStatusException if the input format is invalid.
     */
    @Operation(summary = "Parse a compact Persian date string (YYYYMMDD) to Gregorian LocalDateTime")
    @GetMapping("/parse-from-compact-string")
    public ResponseEntity<LocalDateTime> parseFromCompactString(
            @Parameter(description = "Compact Persian date string, e.g., 14010515")
            @RequestParam String persianDateString) {
        try {
            return ResponseEntity.ok(PersianDateUtil.parseFromCompactString(persianDateString));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid compact date format. Expected YYYYMMDD.",
                    e
            );
        }
    }

    /**
     * Parses a Persian date string with default separators ("/", " ", ":") to a {@link LocalDateTime}.
     *
     * @param persianDateString The string to parse (e.g., "1401/05/15 14:30:00").
     * @return Parsed {@link LocalDateTime}.
     * @throws ResponseStatusException if the input format is invalid.
     */
    @Operation(summary = "Parse a Persian date string with default separators to Gregorian LocalDateTime")
    @GetMapping("/parse-from-string-default")
    public ResponseEntity<LocalDateTime> parseFromStringDefault(
            @Parameter(description = """
                    Persian date string with default separators (/, space, :), e.g., 1401/05/15 14:30:00
                    """)
            @RequestParam String persianDateString) {
        try {
            return ResponseEntity.ok(PersianDateUtil.parseFromString(persianDateString));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: " + persianDateString, e);
        }
    }

    /**
     * Parses a Persian date string with custom separators to a {@link LocalDateTime}.
     *
     * @param persianDateString The string to parse.
     * @param dateSeparator     Separator for date components (e.g., "-").
     * @param dateTimeSeparator Separator between date and time (e.g., "T").
     * @param timeSeparator     Separator for time components (e.g., ":").
     * @return Parsed {@link LocalDateTime}.
     * @throws ResponseStatusException if the input format is invalid.
     */
    @Operation(summary = "Parse a Persian date string with custom separators to Gregorian LocalDateTime")
    @GetMapping("/parse-from-string-custom")
    public ResponseEntity<LocalDateTime> parseFromStringCustom(
            @Parameter(description = "Persian date string, e.g., 1401-05-15T14.30.00")
            @RequestParam String persianDateString,
            @Parameter(description = "Separator for date components (e.g., -)", example = "-")
            @RequestParam(defaultValue = "/") String dateSeparator,
            @Parameter(description = "Separator between date and time (e.g., T)", example = " ")
            @RequestParam(defaultValue = " ") String dateTimeSeparator,
            @Parameter(description = "Separator for time components (e.g., .)", example = ":")
            @RequestParam(defaultValue = ":") String timeSeparator) {
        try {
            return ResponseEntity.ok(
                    PersianDateUtil.parseFromString(persianDateString, dateSeparator, dateTimeSeparator, timeSeparator)
            );
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: " + persianDateString, e);
        }
    }

    /**
     * Calculates the end of the day (00:00:00 of the next day) for the given Gregorian temporal.
     *
     * @param temporal The Gregorian temporal object (e.g., {@link LocalDate} or {@link LocalDateTime}).
     * @return End of the day as {@link LocalDateTime}.
     */
    @Operation(summary = "Calculate the end of the day for a given Gregorian temporal")
    @GetMapping("/end-of-day")
    public ResponseEntity<LocalDateTime> endOfDay(
            @Parameter(description = "Gregorian date or datetime, e.g., 2023-05-15 or 2023-05-15T10:30:45")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime temporal) {
        return ResponseEntity.ok(PersianDateUtil.endOfDay(temporal));
    }

    /**
     * Adds months to a Gregorian {@link LocalDate} using Persian calendar logic.
     *
     * @param baseDate The base Gregorian date.
     * @param months   Number of months to add (negative values subtract).
     * @return Adjusted Gregorian date.
     */
    @Operation(summary = "Add months to a Gregorian date based on Persian calendar rules")
    @GetMapping("/add-months")
    public ResponseEntity<LocalDate> addMonths(
            @Parameter(description = "Base Gregorian date, e.g., 2023-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate,
            @Parameter(description = "Number of months to add (can be negative)")
            @RequestParam int months) {
        return ResponseEntity.ok(PersianDateUtil.addMonths(baseDate, months));
    }

    /**
     * Adds days to a Gregorian {@link LocalDate} using Persian calendar logic.
     *
     * @param baseDate The base Gregorian date.
     * @param days     Number of days to add (negative values subtract).
     * @return Adjusted Gregorian date.
     */
    @Operation(summary = "Add days to a Gregorian date based on Persian calendar rules")
    @GetMapping("/add-days")
    public ResponseEntity<LocalDate> addDays(
            @Parameter(description = "Base Gregorian date, e.g., 2023-05-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate baseDate,
            @Parameter(description = "Number of days to add (can be negative)")
            @RequestParam int days) {
        return ResponseEntity.ok(PersianDateUtil.addDays(baseDate, days));
    }
}
