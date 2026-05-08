package dev.pedrohfreitas.javalib.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationUtilsTest {

    @Test
    @DisplayName("should return true when list is null")
    void shouldReturnTrueWhenListIsNull() {
        assertThat(ValidationUtils.isEmpty((List<?>) null)).isTrue();
    }

    @Test
    @DisplayName("should return true when list is empty")
    void shouldReturnTrueWhenListIsEmpty() {
        assertThat(ValidationUtils.isEmpty(Collections.emptyList())).isTrue();
    }

    @Test
    @DisplayName("should return false when list has elements")
    void shouldReturnFalseWhenListHasElements() {
        assertThat(ValidationUtils.isEmpty(List.of("a", "b"))).isFalse();
    }

    @Test
    @DisplayName("should return false when list is not empty")
    void shouldReturnFalseWhenListIsNotEmpty() {
        assertThat(ValidationUtils.isNotEmpty(List.of(1))).isTrue();
    }

    @Test
    @DisplayName("should return false for null in isNotEmpty")
    void shouldReturnFalseForNullInIsNotEmpty() {
        assertThat(ValidationUtils.isNotEmpty((List<?>) null)).isFalse();
    }

    @Test
    @DisplayName("should return true when map is null")
    void shouldReturnTrueWhenMapIsNull() {
        assertThat(ValidationUtils.isEmpty((Map<?, ?>) null)).isTrue();
    }

    @Test
    @DisplayName("should return true when map is empty")
    void shouldReturnTrueWhenMapIsEmpty() {
        assertThat(ValidationUtils.isEmpty(Collections.emptyMap())).isTrue();
    }

    @Test
    @DisplayName("should return false when map has entries")
    void shouldReturnFalseWhenMapHasEntries() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);
        assertThat(ValidationUtils.isEmpty(map)).isFalse();
    }

    @Test
    @DisplayName("should return true for null string in isBlank")
    void shouldReturnTrueForNullString() {
        assertThat(ValidationUtils.isBlank(null)).isTrue();
    }

    @Test
    @DisplayName("should return true for empty string in isBlank")
    void shouldReturnTrueForEmptyString() {
        assertThat(ValidationUtils.isBlank("")).isTrue();
    }

    @Test
    @DisplayName("should return true for whitespace string in isBlank")
    void shouldReturnTrueForWhitespaceString() {
        assertThat(ValidationUtils.isBlank("   ")).isTrue();
    }

    @Test
    @DisplayName("should return false for non-blank string in isBlank")
    void shouldReturnFalseForNonBlankString() {
        assertThat(ValidationUtils.isBlank("hello")).isFalse();
    }

    @Test
    @DisplayName("should return true for non-null non-blank string")
    void shouldReturnTrueForNonBlankString() {
        assertThat(ValidationUtils.isNotBlank("hello")).isTrue();
    }

    @Test
    @DisplayName("should return false for blank string in isNotBlank")
    void shouldReturnFalseForBlankString() {
        assertThat(ValidationUtils.isNotBlank("")).isFalse();
        assertThat(ValidationUtils.isNotBlank(null)).isFalse();
        assertThat(ValidationUtils.isNotBlank("   ")).isFalse();
    }
}
