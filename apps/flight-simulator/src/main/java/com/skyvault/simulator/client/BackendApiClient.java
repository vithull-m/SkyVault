package com.skyvault.simulator.client;

import com.skyvault.simulator.config.SimulatorConfig;
import com.skyvault.simulator.model.TelemetryPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class BackendApiClient {

    private final RestTemplate restTemplate;
    private final SimulatorConfig simulatorConfig;

    public BackendApiClient(RestTemplate restTemplate, SimulatorConfig simulatorConfig) {
        this.restTemplate = restTemplate;
        this.simulatorConfig = simulatorConfig;
    }

    /**
     * Transmits a telemetry frame to the backend REST API with automatic Spring Retry logic.
     */
    @Retryable(
            retryFor = { RestClientException.class, Exception.class },
            maxAttemptsExpression = "${simulator.max-retry-attempts:3}",
            backoff = @Backoff(delayExpression = "${simulator.retry-delay-ms:2000}")
    )
    public boolean sendTelemetry(TelemetryPayload payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", simulatorConfig.getAuthToken());

        HttpEntity<TelemetryPayload> requestEntity = new HttpEntity<>(payload, headers);

        log.info("✈️ [TRANSMITTING TELEMETRY] Flight: {} | Phase: {} | Alt: {} ft | Speed: {} kts | Time: {}",
                payload.getFlightId(),
                payload.getFlightPhase(),
                payload.getAltitudeFt(),
                payload.getAirspeedKts(),
                payload.getTimestamp()
        );

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    simulatorConfig.getBackendUrl(),
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ [ACK RECEIVED] Telemetry frame successfully recorded on SkyVault Cloud Engine.");
                return true;
            } else {
                log.warn("⚠️ [BACKEND WARNING] Received non-200 HTTP status: {}", response.getStatusCode());
                return false;
            }
        } catch (RestClientException ex) {
            log.error("❌ [TRANSMISSION FAILURE] Error connecting to backend at {}: {}",
                    simulatorConfig.getBackendUrl(), ex.getMessage());
            throw ex; // Re-throw to trigger Spring Retry mechanism
        }
    }

    /**
     * Fallback method executed when all retry attempts fail.
     */
    @Recover
    public boolean recoverTelemetryFailure(Exception ex, TelemetryPayload payload) {
        log.error("🚨 [RECOVERY FALLBACK] Max retry attempts reached for telemetry timestamp {}. Buffering frame locally. Reason: {}",
                payload.getTimestamp(), ex.getMessage());
        return false;
    }
}
