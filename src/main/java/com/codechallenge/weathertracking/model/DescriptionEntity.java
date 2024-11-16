package com.codechallenge.weathertracking.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "description_table")
public class DescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int weatherId;

    private String main;

    private String description;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "weather_entity_id", nullable = false)
    private WeatherConditionEntity weather;
}
