package com.skyvault.repository;

import com.skyvault.model.FlightTelemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TelemetryRepository extends MongoRepository<FlightTelemetry, Long> {

    Page<FlightTelemetry> findByFlightId(String flightId, Pageable pageable);

    Optional<FlightTelemetry> findFirstByFlightIdOrderByTimestampDesc(String flightId);

    Page<FlightTelemetry> findByAircraftId(UUID aircraftId, Pageable pageable);

    Page<FlightTelemetry> findByFlightIdAndTimestampBetween(String flightId, Instant start, Instant end, Pageable pageable);

    void deleteByFlightId(String flightId);
}
