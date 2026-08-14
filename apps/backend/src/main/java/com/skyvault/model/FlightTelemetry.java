package com.skyvault.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "flight_telemetry")
@CompoundIndexes({
        @CompoundIndex(name = "idx_telemetry_flight_time", def = "{'flight_id': 1, 'timestamp': -1}"),
        @CompoundIndex(name = "idx_telemetry_aircraft", def = "{'aircraft_id': 1}")
})
public class FlightTelemetry {

    @Id
    private Long id;

    @Indexed
    @Field("flight_id")
    private String flightId;

    @Indexed
    @Field("aircraft_id")
    private UUID aircraftId;

    @Field("timestamp")
    private Instant timestamp;

    @Field("flight_phase")
    private String flightPhase;

    // Kinematics & Aerodynamics
    @Field("latitude")
    private Double latitude;

    @Field("longitude")
    private Double longitude;

    @Field("altitude_ft")
    private Double altitudeFt;

    @Field("airspeed_kts")
    private Double airspeedKts;

    @Field("heading_deg")
    private Double headingDeg;

    @Field("vertical_speed_fpm")
    private Double verticalSpeedFpm;

    // Engine & Propulsion
    @Field("fuel_level_lbs")
    private Double fuelLevelLbs;

    @Field("engine_rpm")
    private Double engineRpm;

    @Field("engine_temp_c")
    private Double engineTempC;

    // Environment & Systems
    @Field("oat_c")
    private Double oatC;

    @Field("cabin_pressure_psi")
    private Double cabinPressurePsi;

    @Field("battery_volts")
    private Double batteryVolts;

    // Controls
    @Field("landing_gear_status")
    private String landingGearStatus;

    @Field("flaps_degrees")
    private Double flapsDegrees;

    @Field("autopilot_engaged")
    private Boolean autopilotEngaged;

    @Field("hash_signature")
    private String hashSignature;

    @CreatedDate
    @Field("recorded_at")
    private LocalDateTime recordedAt;
}
