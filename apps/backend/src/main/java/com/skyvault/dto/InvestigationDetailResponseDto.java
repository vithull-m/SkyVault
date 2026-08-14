package com.skyvault.dto;

import com.skyvault.model.InvestigationNote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigationDetailResponseDto {
    private String flightId;
    private AircraftResponseDto aircraftDetails;
    private FlightChainVerificationResponseDto integrityStatus;
    private List<TelemetryResponseDto> telemetryHistory;
    private List<InvestigationNote> notes;
    private String evidenceSummary;
    private int totalTelemetryFrames;
}
