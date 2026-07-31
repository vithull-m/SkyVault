package com.skyvault.service;

import com.skyvault.dto.PageResponseDto;
import com.skyvault.dto.TelemetryIngestRequestDto;
import com.skyvault.dto.TelemetryResponseDto;

import java.time.Instant;
import java.util.UUID;

public interface TelemetryService {
    TelemetryResponseDto ingestTelemetry(TelemetryIngestRequestDto dto);
    TelemetryResponseDto getLatestTelemetry(String flightId);
    PageResponseDto<TelemetryResponseDto> getTelemetryByFlightId(String flightId, int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDto<TelemetryResponseDto> getTelemetryByAircraftId(UUID aircraftId, int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDto<TelemetryResponseDto> getTelemetryByTimeRange(String flightId, Instant startTime, Instant endTime, int pageNo, int pageSize, String sortBy, String sortDir);
    int deleteTelemetryByFlightId(String flightId);
}
