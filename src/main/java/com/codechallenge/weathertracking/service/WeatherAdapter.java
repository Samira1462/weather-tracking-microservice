package com.codechallenge.weathertracking.service;

import com.codechallenge.weathertracking.dto.apidto.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class WeatherAdapter {
    private final Logger logger = LoggerFactory.getLogger(WeatherAdapter.class);

    private final WebClient weatherClient;

    public WeatherAdapter(WebClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public Optional<WeatherResponse> getByPostalCode(String postalCode) {
        WeatherResponse weatherResponse;
        try {
            weatherResponse = weatherClient.get()
                    .uri(uriBuilder -> uriBuilder.queryParam("zip", postalCode + "," + "us").build())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), ClientResponse::createException)
                    .bodyToMono(WeatherResponse.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        logger.error("Open Weather Map is not reachable: {}", ex.getMessage());
                        return Mono.error(new WebClientResponseException("Failed to fetch weather data", ex.getStatusCode().value(), ex.getStatusText(), ex.getHeaders(), null, null));
                    })
                    .block();

        } catch (Exception e) {
            logger.error("Exception occurred during weather data fetch: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return Optional.ofNullable(weatherResponse);
    }
}
