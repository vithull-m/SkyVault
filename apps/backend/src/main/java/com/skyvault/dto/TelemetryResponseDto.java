package com.skyvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryResponseDto {
    private Long id;
    private String flightId;
    private UUID aircraftId;
    private Instant timestamp;
    private String flightPhase;

    private Double latitude;
    private Double longitude;
    private Double altitudeFt;
    private Double airspeedKts;
    private Double headingDeg;
    private Double verticalSpeedFpm;

    private Double fuelLevelLbs;
    private Double engineRpm;
    private Double engineTempC;

    private Double oatC;
    private Double cabinPressurePsi;
    private Double batteryVolts;

    private String landingGearStatus;
    private Double flapsDegrees;
    private Boolean autopilotEngaged;
    private String hashSignature;

    private LocalDateTime recordedAt;
}
