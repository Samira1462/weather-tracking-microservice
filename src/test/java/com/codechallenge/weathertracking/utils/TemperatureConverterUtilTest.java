package com.codechallenge.weathertracking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class TemperatureConverterUtilTest {
    @Test
    void testConvertToCelsius() {
        double kelvin = 300.0;
        double expectedCelsius = 26.85;

        double actual = TemperatureConverterUtil.convertToCelsius(kelvin);

        assertEquals(expectedCelsius, actual, 0.001, "Conversion from Kelvin to Celsius is incorrect");
    }

    @Test
    void testConvertToFahrenheit() {
        double kelvin = 300.0;
        double expectedFahrenheit = 80.33;

        double actual = TemperatureConverterUtil.convertToFahrenheit(kelvin);

        assertEquals(expectedFahrenheit, actual, 0.001, "Conversion from Kelvin to Fahrenheit is incorrect");
    }
}
