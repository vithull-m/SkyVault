package com.skyvault.service;

import com.skyvault.dto.*;
import com.skyvault.model.InvestigationNote;

public interface InvestigationService {
    PageResponseDto<TelemetryResponseDto> searchInvestigations(InvestigationSearchRequestDto searchDto);
    InvestigationDetailResponseDto getInvestigationDetails(String flightId);
    InvestigationNote saveInvestigationNote(String flightId, SaveNoteRequestDto requestDto, String investigatorUsername);
    InvestigationReportResponseDto generateReport(String flightId);
}
