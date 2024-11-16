package com.codechallenge.weathertracking.dto.apidto;

public record Sys(
        int type,
        int id,
        double message,
        String country,
        long sunrise,
        long sunset
) {
}
