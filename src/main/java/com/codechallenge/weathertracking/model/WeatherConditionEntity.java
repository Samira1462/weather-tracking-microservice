package com.codechallenge.weathertracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather_table")
public class WeatherConditionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;

    private Double feelsLike;

    private Double tempMin;

    private Double tempMax;

    private Integer humidity;

    private Integer pressure;

    private Double windSpeed;

    private Integer windDeg;

    private Integer cloudCoverage;

    private String cityName;

    private String country;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DescriptionEntity> descriptions;
}
