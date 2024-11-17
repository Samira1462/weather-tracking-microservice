package com.codechallenge.weathertracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
