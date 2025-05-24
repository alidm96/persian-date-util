Persian Date Utility APIA comprehensive Java utility class and Spring Boot REST API for handling Persian (Jalali) calendar operations. This project provides functionalities for converting between Gregorian and Jalali dates, performing date arithmetic, formatting, and parsing, with built-in support for the "Asia/Tehran" timezone.FeaturesDate Conversion: Seamlessly convert dates between Gregorian and Persian (Jalali) calendars.Leap Year Detection: Determine if a given Persian year is a leap year.Date Formatting: Format LocalDateTime objects into various Persian date and time string representations.Date Parsing: Parse Persian date strings (both compact and custom-separated) back into LocalDateTime objects.Date Arithmetic: Add or subtract days and months from LocalDate objects, respecting Persian calendar rules.Timezone Handling: All operations are adjusted for the "Asia/Tehran" timezone.REST API: Exposes core functionalities via a RESTful API.Swagger UI: Interactive API documentation available via Swagger UI.Project Structure├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── tiddev
│   │   │           └── persian_date_util
│   │   │               ├── PersianDateUtilityApplication.java (Main Spring Boot application)
│   │   │               ├── config
│   │   │               │   └── OpenApiConfig.java (Swagger/OpenAPI configuration)
│   │   │               ├── util
│   │   │               │   └── PersianDateUtil.java (Core date utility)
│   │   │               └── web
│   │   │                   └── controller
│   │   │                       └── PersianDateController.java (REST API endpoints)
│   │   └── resources
│   │       └── application.yml (Spring Boot and Swagger configuration)
│   └── test
│       └── java
│           └── com
│               └── tiddev
│                   └── persian_date_util
│                       └── util
│                           └── PersianDateUtilTest.java (Unit tests)
├── pom.xml
└── README.md
Installation and SetupThis is a Spring Boot application.Prerequisites:Java 17 or higherMaven 3.xA JalaliCalendar implementation (e.g., from com.tiddev.util as indicated in the PersianDateUtil imports). Ensure this dependency is correctly resolved in your pom.xml.Dependencies (from pom.xml):Ensure your pom.xml includes these key dependencies:<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.5.0</version> </dependency>
    <dependency>
        <groupId>com.tiddev.util</groupId>
        <artifactId>jalali-calendar</artifactId>
        <version>1.3.4</version> </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
application.yml Configuration (src/main/resources/application.yml):server:
  port: 8080 # The port on which the application will run

spring:
  application:
    name: persian-date-api

springdoc:
  swagger-ui:
    path: /swagger-ui.html # Path to access Swagger UI
    disable-swagger-default-url: true
    display-router-url: false # Set to 'true' if you want to see the 'Explore' dropdown
  api-docs:
    path: /v3/api-docs # Path for the OpenAPI JSON definition
  packages-to-scan: com.tiddev.persian_date_util.web.controller # IMPORTANT: Ensure this matches your controller's package
  paths-to-match: /api/** # Paths to include in documentation
Main Application Class (src/main/java/com/tiddev/persian_date_util/PersianDateUtilityApplication.java):Ensure your main Spring Boot application class has the correct @ComponentScan to discover your controllers and OpenAPI configuration.package com.tiddev.persian_date_util;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // Important import

@SpringBootApplication
@ComponentScan(basePackages = "com.tiddev.persian_date_util") // Scans this package and all its sub-packages
public class PersianDateUtilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersianDateUtilityApplication.class, args);
    }
}
OpenAPI Definition (src/main/java/com/tiddev/persian_date_util/config/OpenApiConfig.java):For customizing the Swagger UI header (title, version, description):package com.tiddev.persian_date_util.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Persian Date Utility API", // Your custom title
        version = "1.0.0",                 // Your API version
        description = "API for comprehensive Persian (Jalali) calendar operations, including date conversions, formatting, parsing, and arithmetic.",
        termsOfService = "http://example.com/terms/",
        contact = @Contact(
            name = "Your Name",
            email = "your.email@example.com",
            url = "http://yourwebsite.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Local Development Server"
        )
    }
)
public class OpenApiConfig {
    // No methods needed here
}
Running the ApplicationBuild the project:Navigate to your project's root directory in your terminal and run:mvn clean install
Run the Spring Boot application:mvn spring-boot:run
Alternatively, you can run the PersianDateUtilityApplication class directly from your IDE (e.g., IntelliJ IDEA, Eclipse).The application will start on http://localhost:8080 (or the port configured in application.yml).Accessing the API and Swagger UIOnce the application is running:Swagger UI (API Documentation): Open your web browser and go to:http://localhost:8080/swagger-ui.htmlHere you can see all the exposed API endpoints, their descriptions, and test them directly.API Endpoints: You can interact with the API directly using tools like Postman, curl, or your browser. For example:http://localhost:8080/api/persian-date/is-current-year-leaphttp://localhost:8080/api/persian-date/convert-to-persian-date-default?gregorianTemporal=2023-05-15T10:00:00Usage (Code Examples - PersianDateUtil Class)The PersianDateUtil class can also be used directly within your Java code for non-API-based date operations.import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.text.ParseException;
import com.tiddev.persian_date_util.util.PersianDateUtil; // Adjust import if needed

public class Example {
    public static void main(String[] args) {
        // Checking Leap Years
        LocalDate date = LocalDate.of(2024, 3, 20); // Gregorian date for 1403/01/01
        boolean isLeap = PersianDateUtil.isLeapYear(date);
        System.out.println("Is 1403 a Persian leap year? " + isLeap); // true
        System.out.println("Is current year a Persian leap year? " + PersianDateUtil.isCurrentYearLeap());

        System.out.println("\n--- Date Conversion ---");
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

        System.out.println("\n--- Date Parsing ---");
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

        System.out.println("\n--- Date Arithmetic ---");
        LocalDate baseDate = LocalDate.of(2023, Month.MAY, 15); // Gregorian date for 1402/02/25

        // Add months
        LocalDate futureDateMonths = PersianDateUtil.addMonths(baseDate, 5);
        System.out.println("Date after adding 5 months: " + futureDateMonths); // Gregorian date for 1402/07/25 (October 17, 2023)

        // Add days
        LocalDate futureDateDays = PersianDateUtil.addDays(baseDate, 10);
        System.out.println("Date after adding 10 days: " + futureDateDays); // Gregorian date for 1402/03/04 (May 25, 2023)
    }
}
ContributingContributions are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.LicenseThis project is licensed under the MIT License.