package com.skyvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_telemetry", indexes = {
        @Index(name = "idx_telemetry_flight_id", columnList = "flight_id"),
        @Index(name = "idx_telemetry_aircraft_id", columnList = "aircraft_id"),
        @Index(name = "idx_telemetry_flight_time", columnList = "flight_id, timestamp DESC")
})
public class FlightTelemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telemetry_id")
    private Long id;

    @Column(name = "flight_id", nullable = false, length = 50)
    private String flightId;

    @Column(name = "aircraft_id", nullable = false)
    private UUID aircraftId;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "flight_phase", nullable = false, length = 30)
    private String flightPhase;

    // Kinematics & Aerodynamics
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "altitude_ft", nullable = false)
    private Double altitudeFt;

    @Column(name = "airspeed_kts", nullable = false)
    private Double airspeedKts;

    @Column(name = "heading_deg", nullable = false)
    private Double headingDeg;

    @Column(name = "vertical_speed_fpm", nullable = false)
    private Double verticalSpeedFpm;

    // Engine & Propulsion
    @Column(name = "fuel_level_lbs", nullable = false)
    private Double fuelLevelLbs;

    @Column(name = "engine_rpm", nullable = false)
    private Double engineRpm;

    @Column(name = "engine_temp_c", nullable = false)
    private Double engineTempC;

    // Environment & Systems
    @Column(name = "oat_c", nullable = false)
    private Double oatC;

    @Column(name = "cabin_pressure_psi", nullable = false)
    private Double cabinPressurePsi;

    @Column(name = "battery_volts", nullable = false)
    private Double batteryVolts;

    // Controls
    @Column(name = "landing_gear_status", nullable = false, length = 20)
    private String landingGearStatus;

    @Column(name = "flaps_degrees", nullable = false)
    private Double flapsDegrees;

    @Column(name = "autopilot_engaged", nullable = false)
    private Boolean autopilotEngaged;

    @Column(name = "hash_signature", length = 64)
    private String hashSignature;

    @CreationTimestamp
    @Column(name = "recorded_at", updatable = false)
    private LocalDateTime recordedAt;
}
