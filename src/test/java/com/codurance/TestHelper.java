package com.codurance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestHelper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private TestHelper() {
        // Utility class
    }

    public static LocalDate getDateFrom(String dateStr) {
        return LocalDate.parse(dateStr, FORMATTER);
    }

}
