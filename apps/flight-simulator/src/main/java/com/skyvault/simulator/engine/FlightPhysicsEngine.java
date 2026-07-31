package com.skyvault.simulator.engine;

import com.skyvault.simulator.model.FlightPhase;
import com.skyvault.simulator.model.FlightSessionState;
import com.skyvault.simulator.model.TelemetryPayload;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class FlightPhysicsEngine {

    private final Random random = new Random();

    /**
     * Advances the physical state of the aircraft by 1 second based on the current flight phase.
     */
    public TelemetryPayload updatePhysicsAndGenerateTelemetry(FlightSessionState state, double speedMultiplier) {
        state.setElapsedSeconds(state.getElapsedSeconds() + 1);
        long time = state.getElapsedSeconds();

        // Phase Transitions Matrix
        updateFlightPhase(state, time);

        // Physics Differential Updates based on active phase
        switch (state.getCurrentPhase()) {
            case PRE_FLIGHT -> executePreFlightDynamics(state);
            case TAKEOFF -> executeTakeoffDynamics(state, speedMultiplier);
            case CLIMB -> executeClimbDynamics(state, speedMultiplier);
            case CRUISE -> executeCruiseDynamics(state, speedMultiplier);
            case DESCENT -> executeDescentDynamics(state, speedMultiplier);
            case LANDING -> executeLandingDynamics(state, speedMultiplier);
        }

        // Apply realistic environmental & sensor noise micro-variations
        applySensorNoiseAndFuelConsumption(state);

        // Advance GPS Latitude/Longitude based on speed & heading
        advanceGpsPosition(state, speedMultiplier);

        // Build telemetry payload
        return TelemetryPayload.builder()
                .timestamp(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                .flightId(state.getFlightId())
                .aircraftId(state.getAircraftId())
                .flightPhase(state.getCurrentPhase().name())
                .latitude(round(state.getLatitude(), 6))
                .longitude(round(state.getLongitude(), 6))
                .altitudeFt(round(state.getAltitudeFt(), 2))
                .airspeedKts(round(state.getAirspeedKts(), 2))
                .headingDeg(round(state.getHeadingDeg(), 2))
                .verticalSpeedFpm(round(state.getVerticalSpeedFpm(), 2))
                .fuelLevelLbs(round(state.getFuelLevelLbs(), 2))
                .engineRpm(round(state.getEngineRpm(), 2))
                .engineTempC(round(state.getEngineTempC(), 2))
                .oatC(round(state.getOatC(), 2))
                .cabinPressurePsi(round(state.getCabinPressurePsi(), 2))
                .batteryVolts(round(state.getBatteryVolts(), 2))
                .landingGearStatus(state.getLandingGearStatus())
                .flapsDegrees(round(state.getFlapsDegrees(), 1))
                .autopilotEngaged(state.isAutopilotEngaged())
                .build();
    }

    private void updateFlightPhase(FlightSessionState state, long elapsedSeconds) {
        if (elapsedSeconds <= 15) {
            state.setCurrentPhase(FlightPhase.PRE_FLIGHT);
        } else if (elapsedSeconds <= 45) {
            state.setCurrentPhase(FlightPhase.TAKEOFF);
        } else if (elapsedSeconds <= 120) {
            state.setCurrentPhase(FlightPhase.CLIMB);
        } else if (elapsedSeconds <= 240) {
            state.setCurrentPhase(FlightPhase.CRUISE);
        } else if (elapsedSeconds <= 320) {
            state.setCurrentPhase(FlightPhase.DESCENT);
        } else {
            state.setCurrentPhase(FlightPhase.LANDING);
        }
    }

    private void executePreFlightDynamics(FlightSessionState state) {
        state.setAirspeedKts(0.0);
        state.setVerticalSpeedFpm(0.0);
        state.setEngineRpm(1200.0);
        state.setEngineTempC(520.0);
        state.setFlapsDegrees(15.0); // Set takeoff flaps
        state.setLandingGearStatus("EXTENDED");
        state.setAutopilotEngaged(false);
    }

    private void executeTakeoffDynamics(FlightSessionState state, double speedMultiplier) {
        state.setEngineRpm(9600.0); // TOGA Power
        state.setEngineTempC(840.0);
        
        // Accelerate airspeed down runway
        state.setAirspeedKts(Math.min(160.0, state.getAirspeedKts() + (5.5 * speedMultiplier)));
        
        if (state.getAirspeedKts() > 140.0) { // Vr Rotation speed
            state.setVerticalSpeedFpm(1800.0 * speedMultiplier);
            state.setAltitudeFt(state.getAltitudeFt() + (state.getVerticalSpeedFpm() / 60.0 * speedMultiplier));
        } else {
            state.setVerticalSpeedFpm(0.0);
        }
    }

    private void executeClimbDynamics(FlightSessionState state, double speedMultiplier) {
        state.setEngineRpm(8900.0);
        state.setEngineTempC(780.0);
        state.setLandingGearStatus("RETRACTED");
        state.setFlapsDegrees(0.0);
        state.setAutopilotEngaged(true);

        state.setAirspeedKts(Math.min(290.0, state.getAirspeedKts() + (1.5 * speedMultiplier)));
        state.setVerticalSpeedFpm(2200.0);
        
        double newAltitude = state.getAltitudeFt() + ((2200.0 / 60.0) * speedMultiplier);
        state.setAltitudeFt(Math.min(33000.0, newAltitude));

        // Outside temp drops ~2°C per 1000 ft altitude
        state.setOatC(15.0 - (state.getAltitudeFt() / 1000.0 * 2.0));
        // Cabin pressure differential decreases slightly with altitude
        state.setCabinPressurePsi(Math.max(10.8, 14.7 - (state.getAltitudeFt() / 1000.0 * 0.12)));
    }

    private void executeCruiseDynamics(FlightSessionState state, double speedMultiplier) {
        state.setEngineRpm(7400.0);
        state.setEngineTempC(680.0);
        state.setAltitudeFt(33000.0);
        state.setVerticalSpeedFpm(0.0);
        state.setAirspeedKts(450.0); // Mach 0.78 cruise
        state.setOatC(-51.0);
        state.setCabinPressurePsi(11.2);
        state.setAutopilotEngaged(true);
    }

    private void executeDescentDynamics(FlightSessionState state, double speedMultiplier) {
        state.setEngineRpm(4200.0); // Idle descent
        state.setEngineTempC(560.0);
        state.setVerticalSpeedFpm(-1800.0);
        state.setAirspeedKts(Math.max(220.0, state.getAirspeedKts() - (2.0 * speedMultiplier)));

        double newAltitude = state.getAltitudeFt() + ((-1800.0 / 60.0) * speedMultiplier);
        state.setAltitudeFt(Math.max(1500.0, newAltitude));

        state.setOatC(15.0 - (state.getAltitudeFt() / 1000.0 * 2.0));
        state.setCabinPressurePsi(Math.min(14.7, 10.8 + ((33000.0 - state.getAltitudeFt()) / 1000.0 * 0.12)));
    }

    private void executeLandingDynamics(FlightSessionState state, double speedMultiplier) {
        state.setLandingGearStatus("EXTENDED");
        state.setFlapsDegrees(30.0); // Full landing flaps
        state.setAutopilotEngaged(false);

        if (state.getAltitudeFt() > 12.0) { // Above touchdown elevation
            state.setVerticalSpeedFpm(-600.0);
            state.setAirspeedKts(Math.max(130.0, state.getAirspeedKts() - (1.5 * speedMultiplier)));
            double newAlt = state.getAltitudeFt() + ((-600.0 / 60.0) * speedMultiplier);
            state.setAltitudeFt(Math.max(12.0, newAlt));
        } else {
            // Touchdown & Rollout
            state.setAltitudeFt(12.0);
            state.setVerticalSpeedFpm(0.0);
            state.setAirspeedKts(Math.max(0.0, state.getAirspeedKts() - (12.0 * speedMultiplier))); // Thrust reverse braking
            state.setEngineRpm(1000.0);
        }
    }

    private void applySensorNoiseAndFuelConsumption(FlightSessionState state) {
        // Fuel consumption: ~2.5 lbs per second in cruise/climb
        double burnRate = (state.getEngineRpm() / 9600.0) * 3.2;
        state.setFuelLevelLbs(Math.max(0.0, state.getFuelLevelLbs() - burnRate));

        // Add sensor micro-jitter (+/- 0.2%)
        double jitter = (random.nextDouble() - 0.5) * 0.004;
        state.setBatteryVolts(28.0 + (random.nextDouble() * 0.4));
        state.setHeadingDeg((state.getHeadingDeg() + jitter) % 360.0);
    }

    private void advanceGpsPosition(FlightSessionState state, double speedMultiplier) {
        if (state.getAirspeedKts() > 10.0) {
            // Convert knots to degrees latitude (~1 knot = 0.000277 degrees / sec)
            double distanceDeg = (state.getAirspeedKts() / 3600.0) * 0.0166 * speedMultiplier;
            double rad = Math.toRadians(state.getHeadingDeg());

            state.setLatitude(state.getLatitude() + (distanceDeg * Math.cos(rad)));
            state.setLongitude(state.getLongitude() + (distanceDeg * Math.sin(rad)));
        }
    }

    private double round(double val, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(val * factor) / factor;
    }
}
