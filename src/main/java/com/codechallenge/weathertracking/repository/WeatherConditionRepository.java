package com.codechallenge.weathertracking.repository;

import com.codechallenge.weathertracking.model.WeatherConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WeatherConditionRepository extends JpaRepository<WeatherConditionEntity, Long> { }
