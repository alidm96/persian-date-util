Persian Date Utility
A comprehensive Java utility class for handling Persian (Jalali) calendar operations. This library provides functionalities for converting between Gregorian/Julian and Jalali dates, performing date arithmetic, formatting, and parsing, with built-in support for the "Asia/Tehran" timezone.

Features
Date Conversion: Seamlessly convert dates between Gregorian/Julian and Persian (Jalali) calendars.

Leap Year Detection: Determine if a given Persian year is a leap year.

Date Formatting: Format LocalDateTime objects into various Persian date and time string representations, including compact and custom-separated formats.

Date Parsing: Parse Persian date strings (both compact and custom-separated) back into LocalDateTime objects.

Date Arithmetic: Add or subtract days and months from LocalDate objects, respecting Persian calendar rules.

Timezone Handling: All operations are adjusted for the "Asia/Tehran" timezone.

Installation
To use this utility in your Java project, you'll need to include the PersianDateUtil.java and JalaliCalendar.java (and its dependencies) files in your source path.

Prerequisites:

Java 8 or higher (for java.time API).

A JalaliCalendar implementation (e.g., from com.tiddev.util as indicated in the imports). Ensure this dependency is correctly resolved in your project.

Usage
Here are some examples of how to use the PersianDateUtil class:

Checking Leap Years
import java.time.LocalDate;
import com.tiddev.lon.lgr.common.util.PersianDateUtil;

public class Example {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2024, 3, 20); // Gregorian date for 1403/01/01
        boolean isLeap = PersianDateUtil.isLeapYear(date);
        System.out.println("Is 1403 a Persian leap year? " + isLeap); // true

        System.out.println("Is current year a Persian leap year? " + PersianDateUtil.isCurrentYearLeap());
    }
}

Date Conversion
import java.time.LocalDateTime;
import java.time.Month;
import com.tiddev.lon.lgr.common.util.PersianDateUtil;

public class Example {
    public static void main(String[] args) {
        LocalDateTime gregorianDateTime = LocalDateTime.of(2023, Month.MAY, 15, 10, 30, 45);

        // Convert to Persian date with default separator "/"
        String persianDate = PersianDateUtil.convertToPersianDate(gregorianDateTime);
        System.out.println("Persian Date (default): " + persianDate); // 1402/02/25

        // Convert to Persian date with custom separator "-"
        String persianDateCustom = PersianDateUtil.convertToPersianDate(gregorianDateTime, "-");
        System.out.println("Persian Date (custom): " + persianDateCustom); // 1402-02-25

        // Convert to compact Persian date (YYYYMMDD)
        String compactPersianDate = PersianDateUtil.convertToCompactPersianDate(gregorianDateTime);
        System.out.println("Compact Persian Date: " + compactPersianDate); // 14020225

        // Convert to Persian datetime with custom separators
        String persianDateTime = PersianDateUtil.convertToPersianDateTime(gregorianDateTime, "/", " ", ":");
        System.out.println("Persian DateTime: " + persianDateTime); // 1402/02/25 10:30:45
    }
}

Date Parsing
import java.time.LocalDateTime;
import java.text.ParseException;
import com.tiddev.lon.lgr.common.util.PersianDateUtil;

public class Example {
    public static void main(String[] args) {
        try {
            // Parse from compact string
            String compactDateString = "14020225";
            LocalDateTime parsedDateTime = PersianDateUtil.parseFromCompactString(compactDateString);
            System.out.println("Parsed from compact: " + parsedDateTime); // 2023-05-15T00:00

            // Parse from string with default separators
            String dateTimeString = "1402/02/25 10:30:45";
            LocalDateTime parsedDateTimeDefault = PersianDateUtil.parseFromString(dateTimeString);
            System.out.println("Parsed from default format: " + parsedDateTimeDefault); // 2023-05-15T10:30:45

            // Parse from string with custom separators
            String customDateTimeString = "1403-01-01T00.00.00";
            LocalDateTime parsedDateTimeCustom = PersianDateUtil.parseFromString(customDateTimeString, "-", "T", ".");
            System.out.println("Parsed from custom format: " + parsedDateTimeCustom); // 2024-03-20T00:00
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

Date Arithmetic
import java.time.LocalDate;
import java.time.Month;
import com.tiddev.lon.lgr.common.util.PersianDateUtil;

public class Example {
    public static void main(String[] args) {
        LocalDate baseDate = LocalDate.of(2023, Month.MAY, 15); // Gregorian date for 1402/02/25

        // Add months
        LocalDate futureDateMonths = PersianDateUtil.addMonths(baseDate, 5);
        System.out.println("Date after adding 5 months: " + futureDateMonths); // Gregorian date for 1402/07/25 (October 17, 2023)

        // Add days
        LocalDate futureDateDays = PersianDateUtil.addDays(baseDate, 10);
        System.out.println("Date after adding 10 days: " + futureDateDays); // Gregorian date for 1402/03/04 (May 25, 2023)
    }
}

Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.