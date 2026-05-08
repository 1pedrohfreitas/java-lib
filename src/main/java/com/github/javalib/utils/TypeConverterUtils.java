package com.github.javalib.utils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilitario para conversao de tuplas em Map tipado.
 * Cada {@link Tuple} contem alias, valor bruto (String) e tipo alvo.
 * O resultado eh um Map&lt;String, Object&gt; com valores convertidos para o tipo definido.
 */
public final class TypeConverterUtils {

    private TypeConverterUtils() {
    }

    /**
     * Converte uma unica tupla para o valor tipado.
     */
    public static Object convert(Tuple tuple) {
        if (tuple.value() == null) {
            return null;
        }
        Class<?> type = tuple.type();
        String value = tuple.value();

        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.valueOf(value);
        } else if (type == Long.class || type == long.class) {
            return Long.valueOf(value);
        } else if (type == Double.class || type == double.class) {
            return Double.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.valueOf(value);
        } else if (type == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (type == Float.class || type == float.class) {
            return Float.valueOf(value);
        } else if (type == Short.class || type == short.class) {
            return Short.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    /**
     * Converte uma lista de tuplas para Map&lt;String, Object&gt;.
     */
    public static Map<String, Object> toMap(List<Tuple> tuples) {
        if (tuples == null) {
            throw new IllegalArgumentException("tuples must not be null");
        }
        Map<String, Object> map = new LinkedHashMap<>();
        for (Tuple tuple : tuples) {
            validateAlias(tuple.alias());
            map.put(tuple.alias(), convert(tuple));
        }
        return map;
    }

    /**
     * Converte tuplas (varargs) para Map&lt;String, Object&gt;.
     */
    public static Map<String, Object> toMap(Tuple... tuples) {
        if (tuples == null) {
            throw new IllegalArgumentException("tuples must not be null");
        }
        return toMap(List.of(tuples));
    }

    private static void validateAlias(String alias) {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("alias must not be null or blank");
        }
    }
}
