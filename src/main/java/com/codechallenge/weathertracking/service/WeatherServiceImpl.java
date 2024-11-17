package com.codechallenge.weathertracking.service;

import com.codechallenge.weathertracking.dto.WeatherConditionDto;
import com.codechallenge.weathertracking.dto.WeatherRequestDto;
import com.codechallenge.weathertracking.mapper.WeatherMapper;
import com.codechallenge.weathertracking.repository.UserRepository;
import com.codechallenge.weathertracking.repository.WeatherConditionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.codechallenge.weathertracking.mapper.UserMapper.createUserEntity;
import static com.codechallenge.weathertracking.mapper.WeatherMapper.toWeatherConditionDto;
import static com.codechallenge.weathertracking.mapper.WeatherMapper.toWeatherConditionEntity;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WeatherAdapter weatherAdapter;

    private final WeatherConditionRepository weatherConditionRepository;

    private final UserRepository userRepository;


    public WeatherServiceImpl(WeatherAdapter weatherAdapter, WeatherConditionRepository weatherConditionRepository, UserRepository userRepository) {
        this.weatherAdapter = weatherAdapter;
        this.weatherConditionRepository = weatherConditionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<WeatherConditionDto> save(WeatherRequestDto dto) {

        logger.info("get weather condition by postal code: {}", dto.postalCode());
        var weatherResponse = weatherAdapter.getByPostalCode(dto.postalCode());

        if (weatherResponse.isEmpty()) {
            return Optional.empty();
        }

        var user = userRepository.findByUsername(dto.user())
                .orElseGet(() -> userRepository.save(createUserEntity(dto.user(), dto.postalCode())));

        var weatherCondition = weatherConditionRepository.save(toWeatherConditionEntity(weatherResponse.get(), user));

        return Optional.of(toWeatherConditionDto(weatherCondition));
    }

    @Transactional
    @Override
    public Optional<List<WeatherConditionDto>> getByPostalCode(String postalCode) {
        return userRepository.findByPostalCode(postalCode)
                .map(entity -> entity.getWeathers().stream().map(WeatherMapper::toWeatherConditionDto).toList());
    }

    @Transactional
    @Override
    public Optional<List<WeatherConditionDto>> getByUser(String user) {
        return userRepository.findByUsername(user)
                .map(entity -> entity.getWeathers().stream().map(WeatherMapper::toWeatherConditionDto).toList());
    }

}
