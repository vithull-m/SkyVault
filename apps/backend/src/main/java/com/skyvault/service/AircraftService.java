package com.skyvault.service;

import com.skyvault.dto.AircraftRequestDto;
import com.skyvault.dto.AircraftResponseDto;

import java.util.List;
import java.util.UUID;

public interface AircraftService {
    AircraftResponseDto createAircraft(AircraftRequestDto dto);
    List<AircraftResponseDto> getAllAircraft();
    AircraftResponseDto getAircraftById(UUID id);
    AircraftResponseDto updateAircraft(UUID id, AircraftRequestDto dto);
    void deleteAircraft(UUID id);
}
