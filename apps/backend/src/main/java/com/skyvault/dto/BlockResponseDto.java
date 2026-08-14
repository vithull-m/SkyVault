package com.skyvault.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockResponseDto {
    private Long blockId;
    private Long blockIndex;
    private String flightId;
    private Long telemetryId;
    private String recordHash;
    private String previousHash;
    private String currentHash;
    private Instant timestamp;
    private String verificationStatus;
}
