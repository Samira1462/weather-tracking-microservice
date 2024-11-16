package com.codechallenge.weathertracking.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    private final Logger logger = LoggerFactory.getLogger(WebClientConfig.class.getName());

    @Value("${openweather.api.maxAttempts}")
    private int maxAttempts;

    @Value("${openweather.api.retryDelay}")
    private int retryDelay;

    @Value("${openweather.api.key}")
    private String weatherApiKey;

    @Value("${openweather.api.url}")
    private String weatherApiUrl;

    @Bean
    WebClient weatherClient() {
        return WebClient.builder()
                .filter((request, next) -> next.exchange(request).retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofSeconds(retryDelay))))
                .filter(logRequest())
                .filter(logResponse())
                .baseUrl(weatherApiUrl + "?appid=" + weatherApiKey)
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("WebClient request: {} {} {}", clientRequest.method(), clientRequest.url(), clientRequest.body());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("WebClient response status: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

}
