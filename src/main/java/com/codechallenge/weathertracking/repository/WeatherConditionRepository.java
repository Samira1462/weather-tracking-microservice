package com.codechallenge.weathertracking.repository;

import com.codechallenge.weathertracking.model.WeatherConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WeatherConditionRepository extends JpaRepository<WeatherConditionEntity, Long> { }
