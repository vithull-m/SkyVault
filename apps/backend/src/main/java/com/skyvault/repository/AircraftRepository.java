package com.skyvault.repository;

import com.skyvault.model.Aircraft;
import com.skyvault.model.AircraftStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, UUID> {
    Optional<Aircraft> findByRegistrationNumber(String registrationNumber);
    Boolean existsByRegistrationNumber(String registrationNumber);
    List<Aircraft> findByAirlineName(String airlineName);
    List<Aircraft> findByStatus(AircraftStatus status);
}
