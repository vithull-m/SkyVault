package com.skyvault.simulator.model;

import lombok.Data;

@Data
public class FlightSessionState {
    private String flightId;
    private String aircraftId;
    private FlightPhase currentPhase;
    private long elapsedSeconds;

    // Kinematic variables
    private double latitude;
    private double longitude;
    private double altitudeFt;
    private double airspeedKts;
    private double headingDeg;
    private double verticalSpeedFpm;

    // Systems state variables
    private double fuelLevelLbs;
    private double engineRpm;
    private double engineTempC;
    private double oatC;
    private double cabinPressurePsi;
    private double batteryVolts;
    private String landingGearStatus;
    private double flapsDegrees;
    private boolean autopilotEngaged;

    public FlightSessionState(String flightId, String aircraftId) {
        this.flightId = flightId;
        this.aircraftId = aircraftId;
        this.currentPhase = FlightPhase.PRE_FLIGHT;
        this.elapsedSeconds = 0;

        // Departure Airport Initialization (e.g., JFK Airport: 40.6413° N, 73.7781° W)
        this.latitude = 40.6413;
        this.longitude = -73.7781;
        this.altitudeFt = 12.0;       // Field elevation
        this.airspeedKts = 0.0;
        this.headingDeg = 130.0;      // Runway heading 13L
        this.verticalSpeedFpm = 0.0;

        this.fuelLevelLbs = 18500.0;  // Initial fuel load
        this.engineRpm = 800.0;       // Idle RPM
        this.engineTempC = 450.0;     // Idle EGT
        this.oatC = 15.0;             // Standard atmospheric ground temp
        this.cabinPressurePsi = 14.7; // Sea level pressure
        this.batteryVolts = 28.2;     // Normal DC bus voltage
        this.landingGearStatus = "EXTENDED";
        this.flapsDegrees = 0.0;
        this.autopilotEngaged = false;
    }
}
