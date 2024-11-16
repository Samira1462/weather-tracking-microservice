package com.codechallenge.weathertracking.utils;

public final class TemperatureConverterUtil {
    private TemperatureConverterUtil() {
    }


    public static double convertToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double convertToFahrenheit(double kelvin) {
        return (kelvin - 273.15) * 9 / 5 + 32;
    }
}
