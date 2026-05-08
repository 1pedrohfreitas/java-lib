package com.github.javalib.utils;

/**
 * Tupla com alias (chave), valor bruto (String) e tipo alvo para conversao.
 */
public record Tuple(String alias, String value, Class<?> type) {
}
