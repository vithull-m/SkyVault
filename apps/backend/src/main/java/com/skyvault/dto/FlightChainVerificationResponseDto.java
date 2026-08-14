package com.skyvault.dto;

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
public class FlightChainVerificationResponseDto {
    private String flightId;
    private int totalBlocksAnalyzed;
    private boolean isChainValid;
    private Long tamperedBlockIndex;
    private Long tamperedTelemetryId;
    private String status;
    private String message;
    private List<String> auditLogs;
}
