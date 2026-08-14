package com.skyvault.dto;

import com.skyvault.model.InvestigationNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationReportResponseDto {
    private String reportNumber;
    private LocalDateTime generatedAt;
    private String flightId;
    private AircraftResponseDto aircraftDetails;
    private String flightRoute;
    private int totalFramesRecorded;

    // Timeline Events & AI Findings
    private List<String> timelineEvents;
    private List<String> aiFindings;

    // Blockchain Verification
    private FlightChainVerificationResponseDto integrityResult;

    // Notes & Investigator Signature
    private List<InvestigationNote> finalNotes;
    private String primaryInvestigator;
    private String reportClassification;
}
