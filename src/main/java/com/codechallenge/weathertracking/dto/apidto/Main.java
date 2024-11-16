package com.codechallenge.weathertracking.dto.apidto;

public record Main(double temp,
                   double feels_like,
                   double temp_min,
                   double temp_max,
                   int pressure,
                   int humidity) {
}
