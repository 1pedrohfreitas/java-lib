package dev.pedrohfreitas.javalib.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateUtilsTest {

    private static final String PATTERN = "dd-MM-yyyy";

    @Test
    @DisplayName("should format Date to dd-MM-yyyy string")
    void shouldFormatDateToString() {
        Date date = new Date(125, 0, 15); // 15-01-2025

        String result = DateUtils.format(date);

        assertThat(result).isEqualTo("15-01-2025");
    }

    @Test
    @DisplayName("should format timestamp to dd-MM-yyyy string")
    void shouldFormatTimestampToString() {
        long timestamp = new Date(125, 5, 10).getTime(); // 10-06-2025

        String result = DateUtils.format(timestamp);

        assertThat(result).isEqualTo("10-06-2025");
    }

    @Test
    @DisplayName("should format epoch millis for first day of month")
    void shouldFormatFirstDayOfMonth() {
        long timestamp = new Date(125, 11, 1).getTime(); // 01-12-2025

        String result = DateUtils.format(timestamp);

        assertThat(result).startsWith("01-");
    }

    @Test
    @DisplayName("should parse dd-MM-yyyy string to Date")
    void shouldParseStringToDate() throws Exception {
        Date result = DateUtils.parse("25-12-2025");

        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertThat(sdf.format(result)).isEqualTo("25-12-2025");
    }

    @Test
    @DisplayName("should parse leap year date correctly")
    void shouldParseLeapYearDate() throws Exception {
        Date result = DateUtils.parse("29-02-2024");

        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        assertThat(sdf.format(result)).isEqualTo("29-02-2024");
    }

    @Test
    @DisplayName("should throw for null Date in format")
    void shouldThrowForNullDate() {
        assertThatThrownBy(() -> DateUtils.format((Date) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");
    }

    @Test
    @DisplayName("should throw for null or blank string in parse")
    void shouldThrowForNullOrBlankString() {
        assertThatThrownBy(() -> DateUtils.parse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");
        assertThatThrownBy(() -> DateUtils.parse(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2025-12-25", "25/12/2025", "abc", "25-13-2025", "32-01-2025"})
    @DisplayName("should throw for invalid date format")
    void shouldThrowForInvalidFormat(String invalid) {
        assertThatThrownBy(() -> DateUtils.parse(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid date format");
    }
}
