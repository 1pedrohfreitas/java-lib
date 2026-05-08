package dev.pedrohfreitas.javalib.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TypeConverterUtilsTest {

    @Test
    @DisplayName("should convert single tuple to typed value")
    void shouldConvertSingleTuple() {
        assertThat(TypeConverterUtils.convert(new Tuple("count", "42", Integer.class)))
                .isEqualTo(42);
        assertThat(TypeConverterUtils.convert(new Tuple("name", "hello", String.class)))
                .isEqualTo("hello");
        assertThat(TypeConverterUtils.convert(new Tuple("flag", "true", Boolean.class)))
                .isEqualTo(true);
    }

    @Test
    @DisplayName("should convert list of tuples to map")
    void shouldConvertTupleListToMap() {
        List<Tuple> tuples = List.of(
                new Tuple("name", "John", String.class),
                new Tuple("age", "30", Integer.class),
                new Tuple("active", "true", Boolean.class),
                new Tuple("score", "9.5", Double.class)
        );

        Map<String, Object> result = TypeConverterUtils.toMap(tuples);

        assertThat(result).hasSize(4);
        assertThat(result.get("name")).isEqualTo("John");
        assertThat(result.get("age")).isEqualTo(30);
        assertThat(result.get("active")).isEqualTo(true);
        assertThat(result.get("score")).isEqualTo(9.5);
    }

    @Test
    @DisplayName("should convert varargs tuples to map")
    void shouldConvertVarargsTuplesToMap() {
        Map<String, Object> result = TypeConverterUtils.toMap(
                new Tuple("id", "100", Long.class),
                new Tuple("price", "19.99", BigDecimal.class)
        );

        assertThat(result).hasSize(2);
        assertThat(result.get("id")).isEqualTo(100L);
        assertThat(result.get("price")).isEqualTo(new BigDecimal("19.99"));
    }

    @Test
    @DisplayName("should return empty map for empty list")
    void shouldReturnEmptyMapForEmptyList() {
        Map<String, Object> result = TypeConverterUtils.toMap(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw when alias is null")
    void shouldThrowWhenAliasIsNull() {
        assertThatThrownBy(() -> TypeConverterUtils.toMap(
                new Tuple(null, "value", String.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("alias");
    }

    @Test
    @DisplayName("should throw when alias is blank")
    void shouldThrowWhenAliasIsBlank() {
        assertThatThrownBy(() -> TypeConverterUtils.toMap(
                new Tuple("  ", "value", String.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("alias");
    }

    @Test
    @DisplayName("should throw when tuples list is null")
    void shouldThrowWhenTuplesListIsNull() {
        assertThatThrownBy(() -> TypeConverterUtils.toMap((List<Tuple>) null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("tuples");
    }

    @Test
    @DisplayName("should throw for unsupported type")
    void shouldThrowForUnsupportedType() {
        assertThatThrownBy(() -> TypeConverterUtils.convert(
                new Tuple("data", "value", java.time.LocalDate.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported type");
    }
}
