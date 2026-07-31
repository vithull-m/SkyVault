package com.skyvault.mapper;

import com.skyvault.dto.TelemetryIngestRequestDto;
import com.skyvault.dto.TelemetryResponseDto;
import com.skyvault.model.FlightTelemetry;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class TelemetryMapper {

    public static FlightTelemetry toEntity(TelemetryIngestRequestDto dto) {
        FlightTelemetry telemetry = new FlightTelemetry();
        telemetry.setFlightId(dto.getFlightId());
        telemetry.setAircraftId(dto.getAircraftId());
        telemetry.setTimestamp(Instant.parse(dto.getTimestamp()));
        telemetry.setFlightPhase(dto.getFlightPhase());

        telemetry.setLatitude(dto.getLatitude());
        telemetry.setLongitude(dto.getLongitude());
        telemetry.setAltitudeFt(dto.getAltitudeFt());
        telemetry.setAirspeedKts(dto.getAirspeedKts());
        telemetry.setHeadingDeg(dto.getHeadingDeg());
        telemetry.setVerticalSpeedFpm(dto.getVerticalSpeedFpm());

        telemetry.setFuelLevelLbs(dto.getFuelLevelLbs());
        telemetry.setEngineRpm(dto.getEngineRpm());
        telemetry.setEngineTempC(dto.getEngineTempC());

        telemetry.setOatC(dto.getOatC());
        telemetry.setCabinPressurePsi(dto.getCabinPressurePsi());
        telemetry.setBatteryVolts(dto.getBatteryVolts());

        telemetry.setLandingGearStatus(dto.getLandingGearStatus());
        telemetry.setFlapsDegrees(dto.getFlapsDegrees());
        telemetry.setAutopilotEngaged(dto.getAutopilotEngaged());

        // Generate local SHA-256 integrity signature checksum
        telemetry.setHashSignature(computeSha256Signature(dto));

        return telemetry;
    }

    public static TelemetryResponseDto toResponseDto(FlightTelemetry entity) {
        return TelemetryResponseDto.builder()
                .id(entity.getId())
                .flightId(entity.getFlightId())
                .aircraftId(entity.getAircraftId())
                .timestamp(entity.getTimestamp())
                .flightPhase(entity.getFlightPhase())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .altitudeFt(entity.getAltitudeFt())
                .airspeedKts(entity.getAirspeedKts())
                .headingDeg(entity.getHeadingDeg())
                .verticalSpeedFpm(entity.getVerticalSpeedFpm())
                .fuelLevelLbs(entity.getFuelLevelLbs())
                .engineRpm(entity.getEngineRpm())
                .engineTempC(entity.getEngineTempC())
                .oatC(entity.getOatC())
                .cabinPressurePsi(entity.getCabinPressurePsi())
                .batteryVolts(entity.getBatteryVolts())
                .landingGearStatus(entity.getLandingGearStatus())
                .flapsDegrees(entity.getFlapsDegrees())
                .autopilotEngaged(entity.getAutopilotEngaged())
                .hashSignature(entity.getHashSignature())
                .recordedAt(entity.getRecordedAt())
                .build();
    }

    private static String computeSha256Signature(TelemetryIngestRequestDto dto) {
        try {
            String rawPayload = String.format("%s|%s|%s|%.4f|%.4f|%.2f|%.2f",
                    dto.getFlightId(), dto.getAircraftId(), dto.getTimestamp(),
                    dto.getLatitude(), dto.getLongitude(), dto.getAltitudeFt(), dto.getAirspeedKts());
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPayload.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return "HASH_COMPUTATION_ERROR";
        }
    }
}
