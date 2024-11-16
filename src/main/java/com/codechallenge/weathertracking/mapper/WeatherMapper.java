package com.codechallenge.weathertracking.mapper;

import com.codechallenge.weathertracking.dto.DescriptionDto;
import com.codechallenge.weathertracking.dto.WeatherConditionDto;
import com.codechallenge.weathertracking.dto.apidto.Weather;
import com.codechallenge.weathertracking.dto.apidto.WeatherResponse;
import com.codechallenge.weathertracking.model.DescriptionEntity;
import com.codechallenge.weathertracking.model.UserEntity;
import com.codechallenge.weathertracking.model.WeatherConditionEntity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.codechallenge.weathertracking.utils.TemperatureConverterUtil.convertToCelsius;

public final class WeatherMapper {

    private WeatherMapper() {
    }

    public static WeatherConditionDto toWeatherConditionDto(WeatherConditionEntity entity) {
        return new WeatherConditionDto(
                entity.getId(),
                convertToCelsius(entity.getTemperature()),
                convertToCelsius(entity.getFeelsLike()),
                convertToCelsius(entity.getTempMin()),
                convertToCelsius(entity.getTempMax()),
                entity.getHumidity(),
                entity.getPressure(),
                entity.getWindSpeed(),
                entity.getWindDeg(),
                entity.getCloudCoverage(),
                entity.getCityName(),
                entity.getCountry(),
                entity.getTimestamp(),
                entity.getDescriptions().stream().map(WeatherMapper::toDescriptionDto).toList()
        );
    }

    public static WeatherConditionEntity toWeatherConditionEntity(WeatherResponse weatherResponse, UserEntity user) {
        var entity = new WeatherConditionEntity();
        entity.setTemperature(weatherResponse.main().temp());
        entity.setFeelsLike(weatherResponse.main().feels_like());
        entity.setTempMin(weatherResponse.main().temp_min());
        entity.setTempMax(weatherResponse.main().temp_max());
        entity.setHumidity(weatherResponse.main().humidity());
        entity.setPressure(weatherResponse.main().pressure());
        var descriptions = weatherResponse.weather()
                .stream()
                .map(weather -> toDescriptionEntity(weather, entity))
                .toList();
        entity.setDescriptions(descriptions);
        entity.setWindSpeed(weatherResponse.wind().speed());
        entity.setWindDeg(weatherResponse.wind().deg());
        entity.setCloudCoverage(weatherResponse.clouds().all());
        entity.setCityName(weatherResponse.name());
        entity.setCountry(weatherResponse.sys().country());
        var timestamp = LocalDateTime.ofInstant(Instant.ofEpochSecond(weatherResponse.dt()), ZoneOffset.UTC);
        entity.setTimestamp(timestamp);
        entity.setUser(user);

        return entity;
    }

    public static WeatherConditionEntity toWeatherConditionEntity(WeatherConditionDto dto, UserEntity user) {
        var entity = new WeatherConditionEntity();
        entity.setId(dto.id());
        entity.setTemperature(dto.temperature());
        entity.setFeelsLike(dto.feelsLike());
        entity.setTempMin(dto.tempMin());
        entity.setTempMax(dto.tempMax());
        entity.setHumidity(dto.humidity());
        entity.setPressure(dto.pressure());
        entity.setWindSpeed(dto.windSpeed());
        entity.setWindDeg(dto.windDeg());
        entity.setCloudCoverage(dto.cloudCoverage());
        entity.setCityName(dto.cityName());
        entity.setCountry(dto.country());
        entity.setTimestamp(dto.timestamp());
        entity.setUser(user);
        entity.setDescriptions(dto.descriptions().stream().map(weather -> toDescriptionEntity(weather, entity)).toList());

        return entity;
    }

    private static DescriptionDto toDescriptionDto(DescriptionEntity entity) {
        return new DescriptionDto(
                entity.getId(),
                entity.getWeatherId(),
                entity.getDescription(),
                entity.getMain(),
                entity.getIcon()
        );
    }

    private static DescriptionEntity toDescriptionEntity(Weather weather, WeatherConditionEntity entity) {
        var description = new DescriptionEntity();
        description.setWeatherId(weather.id());
        description.setDescription(weather.description());
        description.setMain(weather.main());
        description.setIcon(weather.icon());
        description.setWeather(entity);
        return description;
    }

    private static DescriptionEntity toDescriptionEntity(DescriptionDto dto, WeatherConditionEntity entity) {
        var description = new DescriptionEntity();
        description.setWeatherId(dto.weatherId());
        description.setDescription(dto.description());
        description.setMain(dto.main());
        description.setIcon(dto.icon());
        description.setWeather(entity);
        return description;
    }


}
