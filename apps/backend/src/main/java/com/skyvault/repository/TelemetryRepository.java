package com.skyvault.repository;

import com.skyvault.model.FlightTelemetry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TelemetryRepository extends JpaRepository<FlightTelemetry, Long> {

    // Fetch telemetry by Flight ID with pagination
    Page<FlightTelemetry> findByFlightId(String flightId, Pageable pageable);

    // Fetch telemetry by Aircraft ID with pagination
    Page<FlightTelemetry> findByAircraftId(UUID aircraftId, Pageable pageable);

    // Fetch latest telemetry for a specific flight
    Optional<FlightTelemetry> findFirstByFlightIdOrderByTimestampDesc(String flightId);

    // Fetch telemetry in a specific timestamp range for a flight with pagination
    Page<FlightTelemetry> findByFlightIdAndTimestampBetween(
            String flightId, Instant startTime, Instant endTime, Pageable pageable
    );

    // Bulk deletion by Flight ID (Admin Operation)
    @Modifying
    @Query("DELETE FROM FlightTelemetry t WHERE t.flightId = :flightId")
    int deleteByFlightId(@Param("flightId") String flightId);
}
