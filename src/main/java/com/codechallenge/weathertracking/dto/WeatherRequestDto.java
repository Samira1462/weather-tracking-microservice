package com.codechallenge.weathertracking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record WeatherRequestDto(
        @NotEmpty(message = "{weatherRequestDto.user.notEmpty}")
        String username,
        @NotEmpty(message = "{weatherRequestDto.postalCode.notEmpty}")
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "{weatherRequestDto.postalCode.invalid}")
        String postalCode
) {
        @JsonCreator
        public WeatherRequestDto(
                @JsonProperty("username") String username,
                @JsonProperty("postalCode") String postalCode) {
                this.username = username;
                this.postalCode = postalCode;
        }
}
