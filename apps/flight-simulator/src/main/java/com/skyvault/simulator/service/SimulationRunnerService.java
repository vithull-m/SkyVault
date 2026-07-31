package com.skyvault.simulator.service;

import com.skyvault.simulator.client.BackendApiClient;
import com.skyvault.simulator.config.SimulatorConfig;
import com.skyvault.simulator.engine.FlightPhysicsEngine;
import com.skyvault.simulator.model.FlightSessionState;
import com.skyvault.simulator.model.TelemetryPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Slf4j
@Service
public class SimulationRunnerService {

    private final FlightPhysicsEngine physicsEngine;
    private final BackendApiClient apiClient;
    private final SimulatorConfig config;

    private FlightSessionState activeSessionState;

    public SimulationRunnerService(FlightPhysicsEngine physicsEngine,
                                   BackendApiClient apiClient,
                                   SimulatorConfig config) {
        this.physicsEngine = physicsEngine;
        this.apiClient = apiClient;
        this.config = config;
    }

    @PostConstruct
    public void initializeSimulation() {
        log.info("🛫 Initializing SkyVault Flight Simulator Engine...");
        log.info("📍 Target Flight ID: {} | Aircraft UUID: {}", config.getFlightId(), config.getAircraftId());
        log.info("⏱️ Simulation Rate: 1 frame / {} ms (Speed Multiplier: {}x)",
                config.getTickRateMs(), config.getSpeedMultiplier());

        this.activeSessionState = new FlightSessionState(config.getFlightId(), config.getAircraftId());
    }

    /**
     * Executes once per second (1000ms), advancing physics and pushing telemetry frame.
     */
    @Scheduled(fixedRateString = "${simulator.tick-rate-ms:1000}")
    public void runSimulationTick() {
        if (activeSessionState == null) {
            return;
        }

        try {
            // 1. Advance Physics Engine by 1 second tick
            TelemetryPayload telemetryFrame = physicsEngine.updatePhysicsAndGenerateTelemetry(
                    activeSessionState,
                    config.getSpeedMultiplier()
            );

            // 2. Stream telemetry to Cloud REST API with retry support
            apiClient.sendTelemetry(telemetryFrame);

        } catch (Exception ex) {
            log.error("💥 Unhandled exception during simulation tick: {}", ex.getMessage(), ex);
        }
    }
}
