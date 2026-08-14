package com.skyvault.controller;

import com.skyvault.dto.*;
import com.skyvault.service.BlockchainIntegrityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blockchain")
public class BlockchainController {

    private final BlockchainIntegrityService blockchainService;

    public BlockchainController(BlockchainIntegrityService blockchainService) {
        this.blockchainService = blockchainService;
    }

    /**
     * Generate Hash & Anchor Block
     * POST /api/v1/blockchain/anchor
     * Access: System Administrator, Airline Operations Team
     */
    @PostMapping("/anchor")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<BlockResponseDto>> anchorRecord(
            @Valid @RequestBody AnchorBlockRequestDto requestDto) {
        BlockResponseDto responseDto = blockchainService.anchorRecord(requestDto);
        return new ResponseEntity<>(
                new ApiResponseDto<>(true, "Telemetry record successfully anchored into cryptographic ledger", responseDto),
                HttpStatus.CREATED
        );
    }

    /**
     * Verify Single Telemetry Record Integrity
     * GET /api/v1/blockchain/verify/record/{telemetryId}
     * Access: Admin, Airline Ops, Government Investigators
     */
    @GetMapping("/verify/record/{telemetryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<RecordVerificationResponseDto>> verifyRecord(@PathVariable Long telemetryId) {
        RecordVerificationResponseDto response = blockchainService.verifyRecord(telemetryId);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Telemetry record verification check completed", response)
        );
    }

    /**
     * Verify Entire Flight Cryptographic Chain Integrity
     * GET /api/v1/blockchain/verify/flight/{flightId}
     * Access: Admin, Airline Ops, Government Investigators
     */
    @GetMapping("/verify/flight/{flightId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<FlightChainVerificationResponseDto>> verifyFlightChain(@PathVariable String flightId) {
        FlightChainVerificationResponseDto response = blockchainService.verifyFlightChain(flightId);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Flight cryptographic chain verification completed", response)
        );
    }

    /**
     * Get Verification History Ledger for Flight
     * GET /api/v1/blockchain/history/{flightId}
     * Access: Admin, Airline Ops, Government Investigators
     */
    @GetMapping("/history/{flightId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_AIRLINE_OPS', 'ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<PageResponseDto<BlockResponseDto>>> getVerificationHistory(
            @PathVariable String flightId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "50") int pageSize) {

        PageResponseDto<BlockResponseDto> history = blockchainService.getVerificationHistory(flightId, pageNo, pageSize);
        return ResponseEntity.ok(
                new ApiResponseDto<>(true, "Blockchain verification history retrieved", history)
        );
    }
}
