package com.codechallenge.weathertracking.repository;

import com.codechallenge.weathertracking.model.WeatherConditionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WeatherConditionRepository extends JpaRepository<WeatherConditionEntity, Long> {

    @Query("SELECT w FROM WeatherConditionEntity w WHERE (:postalCode IS NOT NULL AND w.user.postalCode = :postalCode) OR (:username IS NOT NULL AND w.user.username = :username)")
    Optional<List<WeatherConditionEntity>> findByPostalCodeOrUsername(@Param("postalCode") String postalCode, @Param("username") String username);
}
