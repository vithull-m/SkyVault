package com.skyvault.service;

import com.skyvault.dto.*;

public interface BlockchainIntegrityService {
    BlockResponseDto anchorRecord(AnchorBlockRequestDto requestDto);
    RecordVerificationResponseDto verifyRecord(Long telemetryId);
    FlightChainVerificationResponseDto verifyFlightChain(String flightId);
    PageResponseDto<BlockResponseDto> getVerificationHistory(String flightId, int pageNo, int pageSize);
}
