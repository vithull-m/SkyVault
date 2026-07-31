package com.skyvault.controller;

import com.skyvault.dto.AircraftRequestDto;
import com.skyvault.dto.AircraftResponseDto;
import com.skyvault.dto.ApiResponseDto;
import com.skyvault.service.AircraftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/aircraft")
public class AircraftController {

    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    /**
     * Add New Aircraft
     * POST /api/v1/aircraft
     * Access: System Administrator, Airline Operations Team
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<AircraftResponseDto>> createAircraft(
            @Valid @RequestBody AircraftRequestDto requestDto) {
        AircraftResponseDto responseDto = aircraftService.createAircraft(requestDto);
        return new ResponseEntity<>(
                new ApiResponseDto<>(true, "Aircraft created successfully", responseDto),
                HttpStatus.CREATED
        );
    }

    /**
     * Get All Aircraft
     * GET /api/v1/aircraft
     * Access: Admin, Airline Ops, Government Investigators
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<List<AircraftResponseDto>>> getAllAircraft() {
        List<AircraftResponseDto> list = aircraftService.getAllAircraft();
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Aircraft records retrieved successfully", list)
        );
    }

    /**
     * Get Aircraft by ID
     * GET /api/v1/aircraft/{id}
     * Access: Admin, Airline Ops, Government Investigators
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<AircraftResponseDto>> getAircraftById(@PathVariable UUID id) {
        AircraftResponseDto responseDto = aircraftService.getAircraftById(id);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Aircraft retrieved successfully", responseDto)
        );
    }

    /**
     * Update Aircraft
     * PUT /api/v1/aircraft/{id}
     * Access: System Administrator, Airline Operations Team
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<AircraftResponseDto>> updateAircraft(
            @PathVariable UUID id,
            @Valid @RequestBody AircraftRequestDto requestDto) {
        AircraftResponseDto responseDto = aircraftService.updateAircraft(id, requestDto);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Aircraft updated successfully", responseDto)
        );
    }

    /**
     * Delete Aircraft
     * DELETE /api/v1/aircraft/{id}
     * Access: System Administrator, Airline Operations Team
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<String>> deleteAircraft(@PathVariable UUID id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Aircraft deleted successfully", null)
        );
    }
}
