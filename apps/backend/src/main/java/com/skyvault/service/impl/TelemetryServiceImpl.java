package com.skyvault.service.impl;

import com.skyvault.dto.PageResponseDto;
import com.skyvault.dto.TelemetryIngestRequestDto;
import com.skyvault.dto.TelemetryResponseDto;
import com.skyvault.exception.ResourceNotFoundException;
import com.skyvault.mapper.TelemetryMapper;
import com.skyvault.model.FlightTelemetry;
import com.skyvault.repository.TelemetryRepository;
import com.skyvault.service.TelemetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class TelemetryServiceImpl implements TelemetryService {

    private final TelemetryRepository telemetryRepository;

    public TelemetryServiceImpl(TelemetryRepository telemetryRepository) {
        this.telemetryRepository = telemetryRepository;
    }

    @Override
    @Transactional
    public TelemetryResponseDto ingestTelemetry(TelemetryIngestRequestDto dto) {
        log.info("📥 [INGESTING TELEMETRY] Flight: {} | Phase: {} | Alt: {} ft | Speed: {} kts",
                dto.getFlightId(), dto.getFlightPhase(), dto.getAltitudeFt(), dto.getAirspeedKts());

        FlightTelemetry entity = TelemetryMapper.toEntity(dto);
        FlightTelemetry savedEntity = telemetryRepository.save(entity);

        log.info("✅ [TELEMETRY RECORDED] ID: {} | Hash Checksum: {}", savedEntity.getId(), savedEntity.getHashSignature());
        return TelemetryMapper.toResponseDto(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public TelemetryResponseDto getLatestTelemetry(String flightId) {
        log.info("🔍 Fetching latest telemetry frame for Flight ID: {}", flightId);
        FlightTelemetry entity = telemetryRepository.findFirstByFlightIdOrderByTimestampDesc(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("FlightTelemetry", "flightId", flightId));
        return TelemetryMapper.toResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<TelemetryResponseDto> getTelemetryByFlightId(String flightId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("📖 Fetching telemetry page {} (size {}) for Flight ID: {}", pageNo, pageSize, flightId);
        Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<FlightTelemetry> page = telemetryRepository.findByFlightId(flightId, pageable);
        Page<TelemetryResponseDto> dtoPage = page.map(TelemetryMapper::toResponseDto);
        return PageResponseDto.fromPage(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<TelemetryResponseDto> getTelemetryByAircraftId(UUID aircraftId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("📖 Fetching telemetry page {} for Aircraft UUID: {}", pageNo, aircraftId);
        Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<FlightTelemetry> page = telemetryRepository.findByAircraftId(aircraftId, pageable);
        Page<TelemetryResponseDto> dtoPage = page.map(TelemetryMapper::toResponseDto);
        return PageResponseDto.fromPage(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<TelemetryResponseDto> getTelemetryByTimeRange(String flightId, Instant startTime, Instant endTime, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("⏱️ Querying telemetry range for Flight ID {} between {} and {}", flightId, startTime, endTime);
        Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<FlightTelemetry> page = telemetryRepository.findByFlightIdAndTimestampBetween(flightId, startTime, endTime, pageable);
        Page<TelemetryResponseDto> dtoPage = page.map(TelemetryMapper::toResponseDto);
        return PageResponseDto.fromPage(dtoPage);
    }

    @Override
    @Transactional
    public int deleteTelemetryByFlightId(String flightId) {
        log.warn("🗑️ [ADMIN DELETE] Executing bulk telemetry removal for Flight ID: {}", flightId);
        int deletedCount = telemetryRepository.deleteByFlightId(flightId);
        log.info("✅ Successfully purged {} telemetry frames for Flight ID: {}", deletedCount, flightId);
        return deletedCount;
    }

    private Pageable createPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNo, pageSize, sort);
    }
}
