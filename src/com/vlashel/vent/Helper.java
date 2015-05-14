package com.vlashel.vent;

import java.util.Locale;

/**
 * @author Vlashel
 * @version 1.0
 * @since 04.05.2015.
 */
public class Helper {
    public static double cutPrecision(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    public static double cutPrecision(double value, String format) {
        return Double.valueOf(String.format(Locale.ENGLISH, format, value));
    }
}
