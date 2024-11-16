package com.codechallenge.weathertracking.dto.apidto;

import java.util.List;

public record WeatherResponse(
        Coord coord,
        List<Weather> weather,
        String base,
        Main main,
        int visibility,
        Wind wind,
        Clouds clouds,
        long dt,
        Sys sys,
        int timezone,
        long id,
        String name,
        int cod
) {
}
