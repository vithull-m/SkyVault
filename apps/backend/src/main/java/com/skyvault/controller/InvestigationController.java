package com.skyvault.controller;

import com.skyvault.dto.*;
import com.skyvault.model.InvestigationNote;
import com.skyvault.service.InvestigationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/investigations")
public class InvestigationController {

    private final InvestigationService investigationService;

    public InvestigationController(InvestigationService investigationService) {
        this.investigationService = investigationService;
    }

    /**
     * Search Investigations
     * GET /api/v1/investigations/search
     * Access: ROLE_INVESTIGATOR (Full), ROLE_ADMIN (Read), ROLE_AIRLINE_OPS (Limited Read)
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ROLE_INVESTIGATOR', 'ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<PageResponseDto<TelemetryResponseDto>>> searchInvestigations(
            @ModelAttribute InvestigationSearchRequestDto searchDto) {
        PageResponseDto<TelemetryResponseDto> response = investigationService.searchInvestigations(searchDto);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Investigation search completed", response));
    }

    /**
     * Get Investigation Details
     * GET /api/v1/investigations/{flightId}
     * Access: ROLE_INVESTIGATOR, ROLE_ADMIN, ROLE_AIRLINE_OPS
     */
    @GetMapping("/{flightId}")
    @PreAuthorize("hasAnyAuthority('ROLE_INVESTIGATOR', 'ROLE_ADMIN', 'ROLE_AIRLINE_OPS')")
    public ResponseEntity<ApiResponseDto<InvestigationDetailResponseDto>> getInvestigationDetails(@PathVariable String flightId) {
        InvestigationDetailResponseDto details = investigationService.getInvestigationDetails(flightId);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Investigation file retrieved", details));
    }

    /**
     * Save Investigation Note & Evidence Summary
     * POST /api/v1/investigations/{flightId}/notes
     * Access: Government Investigation Agency ONLY (ROLE_INVESTIGATOR)
     */
    @PostMapping("/{flightId}/notes")
    @PreAuthorize("hasAuthority('ROLE_INVESTIGATOR')")
    public ResponseEntity<ApiResponseDto<InvestigationNote>> saveInvestigationNote(
            @PathVariable String flightId,
            @Valid @RequestBody SaveNoteRequestDto requestDto,
            Authentication authentication) {
        String username = authentication.getName();
        InvestigationNote note = investigationService.saveInvestigationNote(flightId, requestDto, username);
        return new ResponseEntity<>(new ApiResponseDto<>(true, "Investigation note saved successfully", note), HttpStatus.CREATED);
    }

    /**
     * Generate Official Investigation Report
     * GET /api/v1/investigations/{flightId}/report
     * Access: Government Investigation Agency (ROLE_INVESTIGATOR) & System Admin (ROLE_ADMIN)
     */
    @GetMapping("/{flightId}/report")
    @PreAuthorize("hasAnyAuthority('ROLE_INVESTIGATOR', 'ROLE_ADMIN')")
    public ResponseEntity<ApiResponseDto<InvestigationReportResponseDto>> generateReport(@PathVariable String flightId) {
        InvestigationReportResponseDto report = investigationService.generateReport(flightId);
        return ResponseEntity.ok(new ApiResponseDto<>(true, "Official investigation report generated", report));
    }
}
