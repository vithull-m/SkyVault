package com.skyvault.simulator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Configuration
@EnableScheduling
@EnableRetry
@ConfigurationProperties(prefix = "simulator")
public class SimulatorConfig {

    private String backendUrl = "http://localhost:8080/api/v1/telemetry/ingest";
    private String authToken = "Bearer DEMO_TOKEN";
    private String flightId = "FL-2026-0042";
    private String aircraftId = "b4a1c5d9-7e3f-4a2b-9c8d-1e2f3a4b5c6d";
    private long tickRateMs = 1000;
    private double speedMultiplier = 1.0;
    private int maxRetryAttempts = 3;
    private long retryDelayMs = 2000;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
