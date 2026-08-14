package com.skyvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordVerificationResponseDto {
    private Long telemetryId;
    private String flightId;
    private Long blockIndex;
    private boolean isIntact;
    private String storedRecordHash;
    private String recalculatedRecordHash;
    private String storedCurrentHash;
    private String recalculatedCurrentHash;
    private String status;
    private String message;
}
