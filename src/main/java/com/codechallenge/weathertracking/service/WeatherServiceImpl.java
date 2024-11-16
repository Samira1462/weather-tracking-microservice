package com.codechallenge.weathertracking.service;

import com.codechallenge.weathertracking.dto.WeatherConditionDto;
import com.codechallenge.weathertracking.dto.WeatherRequestDto;
import com.codechallenge.weathertracking.dto.apidto.WeatherResponse;
import com.codechallenge.weathertracking.mapper.WeatherMapper;
import com.codechallenge.weathertracking.model.UserEntity;
import com.codechallenge.weathertracking.repository.UserRepository;
import com.codechallenge.weathertracking.repository.WeatherConditionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.codechallenge.weathertracking.mapper.WeatherMapper.toWeatherConditionDto;
import static com.codechallenge.weathertracking.mapper.WeatherMapper.toWeatherConditionEntity;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WebClient weatherClient;

    private final WeatherConditionRepository weatherConditionRepository;

    private final UserRepository userRepository;


    public WeatherServiceImpl(WebClient weatherClient, WeatherConditionRepository weatherConditionRepository, UserRepository userRepository) {
        this.weatherClient = weatherClient;
        this.weatherConditionRepository = weatherConditionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<WeatherConditionDto> save(WeatherRequestDto dto) {
        var weatherResponse = weatherClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("zip", dto.postalCode() + "," + "us").build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        ClientResponse::createException)
                .bodyToMono(WeatherResponse.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    logger.error("WebClientResponseException occurred: {}", ex.getMessage(), ex);
                    return Mono.error(new WebClientResponseException("Failed to fetch weather data",
                            ex.getStatusCode().value(), ex.getStatusText(), ex.getHeaders(), null, null));
                })
                .block();

        if (weatherResponse == null) {
            return Optional.empty();
        }

        var user = userRepository.findByUsername(dto.user())
                .orElseGet(() -> {
                    var userEntity = new UserEntity();
                    userEntity.setUsername(dto.user());
                    userEntity.setPostalCode(dto.postalCode());
                    userEntity.setRequestTime(LocalDateTime.now());
                    return userRepository.save(userEntity);
                });

        var weatherCondition = weatherConditionRepository.save(toWeatherConditionEntity(weatherResponse, user));

        return Optional.of(toWeatherConditionDto(weatherCondition));
    }

    @Override
    public Optional<List<WeatherConditionDto>> getByPostalCode(String postalCode) {
        return userRepository.findByPostalCode(postalCode)
                .map(entity -> entity.getWeathers().stream().map(WeatherMapper::toWeatherConditionDto).toList());
    }

    @Override
    public Optional<List<WeatherConditionDto>> getByUser(String user) {
        return userRepository.findByUsername(user)
                .map(entity -> entity.getWeathers().stream().map(WeatherMapper::toWeatherConditionDto).toList());
    }

}
