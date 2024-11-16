package com.codechallenge.weathertracking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record WeatherRequestDto(
        @NotBlank(message = "{weatherRequestDto.user.notBlank}")
        String user,
        @NotBlank(message = "{weatherRequestDto.postalCode.notEmpty}")
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "{weatherRequestDto.postalCode.invalid}")
        String postalCode
) { }
