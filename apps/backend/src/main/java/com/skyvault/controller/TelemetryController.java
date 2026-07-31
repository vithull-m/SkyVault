package com.skyvault.controller;

import com.skyvault.dto.ApiResponseDto;
import com.skyvault.dto.PageResponseDto;
import com.skyvault.dto.TelemetryIngestRequestDto;
import com.skyvault.dto.TelemetryResponseDto;
import com.skyvault.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/telemetry")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    /**
     * Ingest Telemetry Stream
     * POST /api/v1/telemetry/ingest
     * Access: Admin, Airline Ops, Automated Simulators
     */
    @PostMapping("/ingest")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<TelemetryResponseDto>> ingestTelemetry(
            @Valid @RequestBody TelemetryIngestRequestDto requestDto) {
        TelemetryResponseDto responseDto = telemetryService.ingestTelemetry(requestDto);
        return new ResponseEntity<>(
                new ApiResponseDto<>(true, "Telemetry frame successfully ingested", responseDto),
                HttpStatus.CREATED
        );
    }

    /**
     * Get Latest Telemetry Frame for a Flight
     * GET /api/v1/telemetry/latest/{flightId}
     * Access: Admin, Airline Ops, Investigators
     */
    @GetMapping("/latest/{flightId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<TelemetryResponseDto>> getLatestTelemetry(@PathVariable String flightId) {
        TelemetryResponseDto responseDto = telemetryService.getLatestTelemetry(flightId);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Latest telemetry retrieved successfully", responseDto)
        );
    }

    /**
     * Get Telemetry by Flight ID (Paginated & Sorted)
     * GET /api/v1/telemetry/flight/{flightId}
     * Access: Admin, Airline Ops, Investigators
     */
    @GetMapping("/flight/{flightId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<PageResponseDto<TelemetryResponseDto>>> getTelemetryByFlightId(
            @PathVariable String flightId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {

        PageResponseDto<TelemetryResponseDto> pageResponse = telemetryService.getTelemetryByFlightId(
                flightId, pageNo, pageSize, sortBy, sortDir
        );
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Telemetry stream retrieved successfully", pageResponse)
        );
    }

    /**
     * Get Telemetry by Aircraft ID (Paginated & Sorted)
     * GET /api/v1/telemetry/aircraft/{aircraftId}
     * Access: Admin, Airline Ops, Investigators
     */
    @GetMapping("/aircraft/{aircraftId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<PageResponseDto<TelemetryResponseDto>>> getTelemetryByAircraftId(
            @PathVariable UUID aircraftId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {

        PageResponseDto<TelemetryResponseDto> pageResponse = telemetryService.getTelemetryByAircraftId(
                aircraftId, pageNo, pageSize, sortBy, sortDir
        );
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Aircraft telemetry stream retrieved successfully", pageResponse)
        );
    }

    /**
     * Get Telemetry by Time Range (Paginated & Sorted)
     * GET /api/v1/telemetry/range?flightId=...&startTime=...&endTime=...
     * Access: Admin, Airline Ops, Investigators
     */
    @GetMapping("/range")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<PageResponseDto<TelemetryResponseDto>>> getTelemetryByTimeRange(
            @RequestParam("flightId") String flightId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "timestamp", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

        PageResponseDto<TelemetryResponseDto> pageResponse = telemetryService.getTelemetryByTimeRange(
                flightId, startTime, endTime, pageNo, pageSize, sortBy, sortDir
        );
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Time-range telemetry data retrieved successfully", pageResponse)
        );
    }

    /**
     * Delete Telemetry by Flight ID
     * DELETE /api/v1/telemetry/flight/{flightId}
     * Access: System Administrator ONLY
     */
    @DeleteMapping("/flight/{flightId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<String>> deleteTelemetryByFlightId(@PathVariable String flightId) {
        int deletedCount = telemetryService.deleteTelemetryByFlightId(flightId);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, String.format("Successfully purged %d telemetry records for flight %s", deletedCount, flightId), null)
        );
    }
}
