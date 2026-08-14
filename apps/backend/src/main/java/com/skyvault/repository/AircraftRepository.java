package com.skyvault.repository;

import com.skyvault.model.Aircraft;
import com.skyvault.model.AircraftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AircraftRepository extends MongoRepository<Aircraft, UUID> {

    Optional<Aircraft> findByRegistrationNumber(String registrationNumber);

    Boolean existsByRegistrationNumber(String registrationNumber);

    List<Aircraft> findByAirlineName(String airlineName);

    Page<Aircraft> findByStatus(AircraftStatus status, Pageable pageable);
}
