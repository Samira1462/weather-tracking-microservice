package com.codechallenge.weathertracking.utils;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
class TemperatureConverterUtilTest {
    @Test
    void testConvertToCelsius() {
        var kelvin = 300.0;
        var expectedCelsius = 26.85;

        var actual = TemperatureConverterUtil.convertToCelsius(kelvin);

        assertEquals(expectedCelsius, actual, 0.001, "Conversion from Kelvin to Celsius is incorrect");
    }

    @Test
    void testConvertToFahrenheit() {
        var kelvin = 300.0;
        var expectedFahrenheit = 80.33;

        var actual = TemperatureConverterUtil.convertToFahrenheit(kelvin);

        assertEquals(expectedFahrenheit, actual, 0.001, "Conversion from Kelvin to Fahrenheit is incorrect");
    }
}
