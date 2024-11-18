package com.codechallenge.weathertracking.api;

import com.codechallenge.weathertracking.dto.WeatherConditionDto;
import com.codechallenge.weathertracking.dto.WeatherRequestDto;
import com.codechallenge.weathertracking.service.WeatherService;
import com.codechallenge.weathertracking.utils.HttpUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.codechallenge.weathertracking.utils.ApiErrorUtils.shouldBeValid;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequestMapping("/app")
public class WeatherApi {
    private final Logger logger = LoggerFactory.getLogger(WeatherApi.class);

    private final WeatherService weatherService;

    public WeatherApi(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping("/weather")
    @Validated
    public ResponseEntity<WeatherConditionDto> create(@RequestBody @Valid WeatherRequestDto dto, BindingResult bindingResult) {
        logger.info("Received an inbound request to save a weather condition");
        shouldBeValid(bindingResult);

        return weatherService.save(dto)
                .map(response -> ResponseEntity.created(HttpUtils.uriOf(response.id())).body(response))
                .orElseThrow();
    }

    @GetMapping("/history")
    public ResponseEntity<List<WeatherConditionDto>> getPostalCode(@Valid @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "{weatherDto.postalCode.invalid}")
                                                                   @RequestParam(required = false) String postalCode,
                                                                   @RequestParam(required = false) String user) {
        logger.info("Received an inbound request to get weather history");

        if ((user == null && postalCode == null) || (user != null && postalCode != null)) {
            throw new IllegalArgumentException("You must provide either 'user' or 'postal code', but not both or neither.");
        }

        Optional<List<WeatherConditionDto>> history = Optional.empty();

        if (user != null) {
            logger.info("Retrieving weather conditions for user: [{}]", user);
            history = weatherService.getByUser(user);
        }

        if (postalCode != null) {
            logger.info("Retrieving weather conditions for postal code: [{}]", postalCode);
            history = weatherService.getByPostalCode(postalCode);
        }

        return history.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

}
