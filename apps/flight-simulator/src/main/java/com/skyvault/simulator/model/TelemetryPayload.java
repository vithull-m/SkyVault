package com.skyvault.simulator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryPayload {
    private String timestamp;           // ISO 8601 string format
    private String flightId;            // e.g., "FL-2026-0042"
    private String aircraftId;          // UUID string
    private String flightPhase;         // PRE_FLIGHT, TAKEOFF, CLIMB, CRUISE, DESCENT, LANDING
    
    // Position & Aerodynamics
    private double latitude;            // Degrees (-90 to +90)
    private double longitude;           // Degrees (-180 to +180)
    private double altitudeFt;          // Altitude in feet
    private double airspeedKts;         // Airspeed in knots
    private double headingDeg;          // Heading angle (0 to 360)
    private double verticalSpeedFpm;    // Vertical speed (feet per minute)
    
    // Propulsion & Fuel Systems
    private double fuelLevelLbs;        // Fuel remaining in pounds
    private double engineRpm;           // Engine RPM
    private double engineTempC;         // Engine temperature (°C)
    
    // Environmental & Life Support
    private double oatC;                // Outside Air Temperature (°C)
    private double cabinPressurePsi;    // Cabin pressure (PSI)
    private double batteryVolts;        // Avionics bus voltage (Volts)
    
    // Controls & Flight Control Systems
    private String landingGearStatus;   // "EXTENDED", "RETRACTED", "GEAR_DOWN", "GEAR_UP"
    private double flapsDegrees;        // Flap deflection angle (0°, 15°, 30°)
    private boolean autopilotEngaged;   // Autopilot status flag
}
