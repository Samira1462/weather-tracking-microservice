package com.codechallenge.weathertracking.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WeatherConditionDto(
        Long id,
        Double temperature,
        Double feelsLike,
        Double tempMin,
        Double tempMax,
        Integer humidity,
        Integer pressure,
        Double windSpeed,
        Integer windDeg,
        Integer cloudCoverage,
        String cityName,
        String country,
        LocalDateTime timestamp,
        List<DescriptionDto> descriptions
) {
}
