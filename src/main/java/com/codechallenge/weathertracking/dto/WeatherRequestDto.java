package com.codechallenge.weathertracking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record WeatherRequestDto(
        @NotEmpty(message = "{weatherRequestDto.user.notEmpty}")
        String user,
        @NotEmpty(message = "{weatherRequestDto.postalCode.notEmpty}")
        @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "{weatherRequestDto.postalCode.invalid}")
        String postalCode
) {
        @JsonCreator
        public WeatherRequestDto(
                @JsonProperty("user") String user,
                @JsonProperty("postalCode") String postalCode) {
                this.user = user;
                this.postalCode = postalCode;
        }
}
