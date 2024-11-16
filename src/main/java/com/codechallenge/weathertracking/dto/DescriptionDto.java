package com.codechallenge.weathertracking.dto;

public record DescriptionDto(
        Long id,
        int weatherId,
        String main,
        String description,
        String icon
) {
}
