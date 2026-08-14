package com.skyvault.service.impl;

import com.skyvault.dto.TelemetryIngestRequestDto;
import com.skyvault.dto.TelemetryResponseDto;
import com.skyvault.dto.PageResponseDto;
import com.skyvault.exception.ResourceNotFoundException;
import com.skyvault.mapper.TelemetryMapper;
import com.skyvault.model.FlightTelemetry;
import com.skyvault.repository.TelemetryRepository;
import com.skyvault.service.SequenceGeneratorService;
import com.skyvault.service.TelemetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class TelemetryServiceImpl implements TelemetryService {

    private static final String TELEMETRY_SEQ = "flight_telemetry_sequence";

    private final TelemetryRepository telemetryRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    public TelemetryServiceImpl(TelemetryRepository telemetryRepository,
                                SequenceGeneratorService sequenceGeneratorService) {
        this.telemetryRepository = telemetryRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public TelemetryResponseDto ingestTelemetry(TelemetryIngestRequestDto dto) {
        log.info("📥 [INGESTING TELEMETRY] Flight: {} | Phase: {} | Alt: {} ft | Speed: {} kts",
                dto.getFlightId(), dto.getFlightPhase(), dto.getAltitudeFt(), dto.getAirspeedKts());

        FlightTelemetry entity = TelemetryMapper.toEntity(dto);
        entity.setId(sequenceGeneratorService.generateSequence(TELEMETRY_SEQ));
        FlightTelemetry savedEntity = telemetryRepository.save(entity);

        log.info("✅ [TELEMETRY RECORDED] ID: {} | Hash Checksum: {}", savedEntity.getId(), savedEntity.getHashSignature());
        return TelemetryMapper.toResponseDto(savedEntity);
    }

    @Override
    public TelemetryResponseDto getLatestTelemetry(String flightId) {
        log.info("🔍 Fetching latest telemetry frame for Flight ID: {}", flightId);
        FlightTelemetry entity = telemetryRepository.findFirstByFlightIdOrderByTimestampDesc(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("FlightTelemetry", "flightId", flightId));
        return TelemetryMapper.toResponseDto(entity);
    }

    @Override
    public PageResponseDto<TelemetryResponseDto> getTelemetryByFlightId(String flightId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("📖 Fetching telemetry page {} (size {}) for Flight ID: {}", pageNo, pageSize, flightId);
        Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<FlightTelemetry> page = telemetryRepository.findByFlightId(flightId, pageable);
        Page<TelemetryResponseDto> dtoPage = page.map(TelemetryMapper::toResponseDto);
        return PageResponseDto.fromPage(dtoPage);
    }

    @Override
    public PageResponseDto<TelemetryResponseDto> getTelemetryByAircraftId(UUID aircraftId, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("📖 Fetching telemetry page {} for Aircraft UUID: {}", pageNo, aircraftId);
        Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
        Page<FlightTelemetry> page = telemetryRepository.findByAircraftId(aircraftId, pageable);
        Page<TelemetryResponseDto> dtoPage = page.map(TelemetryMapper::toResponseDto);
        return PageResponseDto.fromPage(dtoPage);
    }

    @Override
    public PageResponseDto<TelemetryResponseDto> getTelemetryByTimeRange(String flightId, Instant startTime, Instant endTime, int pageNo, int pageSize, String sortBy, String sortDir) {
        log.info("⏱️ Querying telemetry range for Flight ID {} between {} and {}", flightId, startTime, endTime);
        Pageable pageable = createPageable(pageNo, pageSize, sortBy, sortDir);
        // Fetch all in range then apply manual paging via Spring Data slice
        Page<FlightTelemetry> page = telemetryRepository.findByFlightIdAndTimestampBetween(flightId, startTime, endTime, pageable);
        Page<TelemetryResponseDto> dtoPage = page.map(TelemetryMapper::toResponseDto);
        return PageResponseDto.fromPage(dtoPage);
    }

    @Override
    public int deleteTelemetryByFlightId(String flightId) {
        log.warn("🗑️ [ADMIN DELETE] Executing bulk telemetry removal for Flight ID: {}", flightId);
        // Count before deletion for return value (MongoDB deleteByX returns void)
        long count = telemetryRepository.findByFlightId(flightId, Pageable.unpaged()).getTotalElements();
        telemetryRepository.deleteByFlightId(flightId);
        log.info("✅ Successfully purged {} telemetry frames for Flight ID: {}", count, flightId);
        return (int) count;
    }

    private Pageable createPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(pageNo, pageSize, sort);
    }
}
