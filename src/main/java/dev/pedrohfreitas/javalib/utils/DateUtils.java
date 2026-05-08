package dev.pedrohfreitas.javalib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilitarios para conversao de datas no formato dd-MM-yyyy.
 */
public final class DateUtils {

    private static final String PATTERN = "dd-MM-yyyy";

    private DateUtils() {
    }

    /**
     * Formata um Date para String no formato dd-MM-yyyy.
     */
    public static String format(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }
        return createFormatter().format(date);
    }

    /**
     * Formata um timestamp (epoch millis) para String no formato dd-MM-yyyy.
     */
    public static String format(long timestamp) {
        return format(new Date(timestamp));
    }

    /**
     * Converte uma String no formato dd-MM-yyyy para Date.
     */
    public static Date parse(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException("date must not be null or blank");
        }
        try {
            return createFormatter().parse(dateStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }

    private static SimpleDateFormat createFormatter() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        return sdf;
    }
}
