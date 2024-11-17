package com.codechallenge.weathertracking.service;

import com.codechallenge.weathertracking.dto.apidto.*;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class WeatherAdapterTest {

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Inject
    WeatherAdapter systemUnderTest;

    @Test
    void givenPostalCode_whenGetByPostalCode_thenSuccess() {

        var givenPostalCode = "10001";
        WeatherResponse weatherResponse = createWeatherResponse();

        when(responseSpec.bodyToMono(WeatherResponse.class)).thenReturn(Mono.just(weatherResponse));

        Optional<WeatherResponse> actual = systemUnderTest.getByPostalCode(givenPostalCode);

        assertTrue(actual.isPresent());
    }

    @Test
    void givenPostalCode_whenGetByPostalCode_thenThrowException() {
        var givenPostalCode = "0";
        var actual = assertThrows(RuntimeException.class, () -> systemUnderTest.getByPostalCode(givenPostalCode));

        assertNotNull(actual);
        assertEquals("Failed to fetch weather data", actual.getMessage());
    }

    private static WeatherResponse createWeatherResponse() {

        Coord coord = new Coord(40.7128, -74.0060);
        Weather weather = new Weather(800, "Clear", "clear sky", "01d");
        Main main = new Main(15.5, 1013, 53, 15.0,15 ,17);
        Wind wind = new Wind(5.1, 200);
        Clouds clouds = new Clouds(0);
        Sys sys = new Sys(1, 1, 2d,"US", 1637109200, 1637152400);

        return new WeatherResponse(
                coord,
                List.of(weather),
                "stations",
                main,
                10000,
                wind,
                clouds,
                1637100000,
                sys,
                -18000,
                5128581,
                "US",
                200
        );
    }


}
