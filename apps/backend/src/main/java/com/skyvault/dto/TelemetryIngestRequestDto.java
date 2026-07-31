package com.skyvault.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryIngestRequestDto {

    @NotBlank(message = "Flight ID is required")
    private String flightId;

    @NotNull(message = "Aircraft ID is required")
    private UUID aircraftId;

    @NotBlank(message = "ISO 8601 timestamp string is required")
    private String timestamp;

    @NotBlank(message = "Flight phase is required")
    private String flightPhase;

    @NotNull(message = "Latitude is required")
    @Min(value = -90, message = "Latitude must be >= -90.0")
    @Max(value = 90, message = "Latitude must be <= 90.0")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Min(value = -180, message = "Longitude must be >= -180.0")
    @Max(value = 180, message = "Longitude must be <= 180.0")
    private Double longitude;

    @NotNull(message = "Altitude is required")
    @Min(value = -1000, message = "Altitude cannot be less than -1000 ft")
    @Max(value = 100000, message = "Altitude exceeds realistic flight boundaries")
    private Double altitudeFt;

    @NotNull(message = "Airspeed is required")
    @Min(value = 0, message = "Airspeed cannot be negative")
    private Double airspeedKts;

    @NotNull(message = "Heading is required")
    @Min(value = 0, message = "Heading must be >= 0")
    @Max(value = 360, message = "Heading must be <= 360")
    private Double headingDeg;

    @NotNull(message = "Vertical speed is required")
    private Double verticalSpeedFpm;

    @NotNull(message = "Fuel level is required")
    @Min(value = 0, message = "Fuel level cannot be negative")
    private Double fuelLevelLbs;

    @NotNull(message = "Engine RPM is required")
    @Min(value = 0, message = "Engine RPM cannot be negative")
    private Double engineRpm;

    @NotNull(message = "Engine temperature is required")
    private Double engineTempC;

    @NotNull(message = "Outside air temperature is required")
    private Double oatC;

    @NotNull(message = "Cabin pressure is required")
    @Min(value = 0, message = "Cabin pressure cannot be negative")
    private Double cabinPressurePsi;

    @NotNull(message = "Battery voltage is required")
    @Positive(message = "Battery voltage must be positive")
    private Double batteryVolts;

    @NotBlank(message = "Landing gear status is required")
    private String landingGearStatus;

    @NotNull(message = "Flaps position in degrees is required")
    @Min(value = 0, message = "Flaps degrees cannot be negative")
    private Double flapsDegrees;

    @NotNull(message = "Autopilot status is required")
    private Boolean autopilotEngaged;
}
